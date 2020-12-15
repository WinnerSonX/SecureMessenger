package com.freedomsoft.securemessenger.api.models;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;

import com.freedomsoft.securemessenger.LiveData;
import com.freedomsoft.securemessenger.R;
import com.freedomsoft.securemessenger.api.MessengerApi;
import com.freedomsoft.securemessenger.api.StompApi;
import com.freedomsoft.securemessenger.databinding.ActivityChatBinding;
import com.freedomsoft.securemessenger.room.models.Chat;
import com.freedomsoft.securemessenger.room.models.User;
import com.freedomsoft.securemessenger.security.Encryptor;
import com.freedomsoft.securemessenger.security.KeyGen;
import com.freedomsoft.securemessenger.ui.adapters.MessagesAdapter;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import lombok.NonNull;
import ua.naiksoftware.stomp.dto.LifecycleEvent;

@SuppressWarnings("unchecked")
public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding b;
    @NonNull
    private Chat chat;
    private MessengerApi messagesApi = MessengerApi.getInstance();
    private User user;
    private MessagesAdapter messagesAdapter;
    private Encryptor encryptor;
    private Disposable sendMessageDisposable;
    private Disposable getMessagesDisposable;
    private Disposable stompLifecycleDisposable;
    private LinearLayoutManager linearLayoutManager;
    private Long afterId;
    private StompApi stompApi = StompApi.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        chat = LiveData.getInstance().currentChat.getValue();
        user = LiveData.getInstance().userMutableLiveData.getValue();
        encryptor = Encryptor.newInstance(chat.getAesKey());
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        stompApi.getStompClient().connect();
        stompLifecycleDisposable = stompApi.getStompClient().lifecycle().observeOn(AndroidSchedulers.mainThread()).subscribe(lifecycleEvent -> {
            if (lifecycleEvent.getType() == LifecycleEvent.Type.CLOSED)
                stompApi.getStompClient().connect();
            if(lifecycleEvent.getType()== LifecycleEvent.Type.OPENED)
            Toast.makeText(this, lifecycleEvent.getType().name(), Toast.LENGTH_SHORT).show();
        });
        b.messagesList.setLayoutManager(linearLayoutManager);
        messagesAdapter = new MessagesAdapter(this, encryptor);
        getMessagesDisposable = Single.defer(() -> messagesApi.getMessages(user, chat, afterId))
//                .repeatWhen(objectObservable -> objectObservable.delay(2000, TimeUnit.MILLISECONDS))
                .retryWhen(throwableObservable -> throwableObservable.delay(2000, TimeUnit.MILLISECONDS)).subscribe(listResponse -> {
                    if (listResponse.isSuccessful() && listResponse.body() != null && !listResponse.body().isEmpty()) {
                        if (afterId == null) {
                            messagesAdapter.setMessageList(listResponse.body());
                            b.messagesList.setAdapter(messagesAdapter);
                            b.messagesList.scrollToPosition(messagesAdapter.getItemCount() - 1);
                            afterId = listResponse.body().get(listResponse.body().size() - 1).getId();
                        } else {
                            messagesAdapter.notifyIncome(listResponse.body());
                            afterId = listResponse.body().get(listResponse.body().size() - 1).getId();
                            LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(this) {

                                @Override
                                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                                    return displayMetrics.density * 48 / 350;

                                }

                            };

                            linearSmoothScroller.setTargetPosition(messagesAdapter.getItemCount() - 1);
                            linearLayoutManager.startSmoothScroll(linearSmoothScroller);

                        }

                    }
                }, t -> {

                });
        getMessagesDisposable = StompApi.getInstance().getStompClient()
                .topic("/messages/" + user.getId())
                .map(stompMessage -> Message.fromPayload(stompMessage.getPayload()))
                .retry()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    messagesAdapter.notifyIncome(Collections.singletonList(message));
                    afterId = message.getId();
                    LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(this) {

                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return displayMetrics.density * 48 / 350;

                        }

                    };

                    linearSmoothScroller.setTargetPosition(messagesAdapter.getItemCount() - 1);
                    linearLayoutManager.startSmoothScroll(linearSmoothScroller);
                }, throwable1 -> {
                    throwable1.printStackTrace();
                });
        b.sendMessageButton.setOnClickListener(v -> {
            byte[] iv = KeyGen.createIV(16, Optional.empty());
            Message message = Message.create(user.getId(),
                    chat.getUserId(),
                    encryptor.encrypt(b.messageField.getText().toString(), iv),
                    iv);
            sendMessageDisposable = StompApi.getInstance().getStompClient().send("/app/chat", message.toPayload()).subscribe(() -> {
                Log.d("STOMP_RESULT", "SUCCESS");
                messagesAdapter.notifySent(message);
                b.messageField.setText("");
                LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(this) {

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return displayMetrics.density * 48 / 350;

                    }

                };

                linearSmoothScroller.setTargetPosition(messagesAdapter.getItemCount() - 1);
                linearLayoutManager.startSmoothScroll(linearSmoothScroller);
            }, throwable -> {

            });

//            sendMessageDisposable = messagesApi
//                    .sendMessage(message)
//                    .subscribe(response -> {
//                        if (response.isSuccessful()) {
//                            b.messageField.setText("");
//                            messagesAdapter.notifySent(message);
//                            LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(this) {
//
//                                @Override
//                                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
//                                    return displayMetrics.density * 48 / 350;
//
//                                }
//
//                            };
//
//                            linearSmoothScroller.setTargetPosition(messagesAdapter.getItemCount() - 1);
//                            linearLayoutManager.startSmoothScroll(linearSmoothScroller);
//
//                        }
//
//                    }, t -> {
//                        Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                    });

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stompApi.getStompClient().disconnect();
        stompLifecycleDisposable.dispose();
        getMessagesDisposable.dispose();
        if (sendMessageDisposable != null)
            sendMessageDisposable.dispose();
    }
}