package com.freedomsoft.securemessenger.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.freedomsoft.securemessenger.ClipboardService;
import com.freedomsoft.securemessenger.Constants;
import com.freedomsoft.securemessenger.LiveData;
import com.freedomsoft.securemessenger.R;
import com.freedomsoft.securemessenger.databinding.ActivityMainBinding;
import com.freedomsoft.securemessenger.room.models.User;
import com.freedomsoft.securemessenger.room.services.ChatService;
import com.freedomsoft.securemessenger.ui.adapters.ChatListAdapter;

import io.reactivex.disposables.Disposable;

import static com.freedomsoft.securemessenger.Constants.NEW_CONVERSATION_FRAGMENT_TAG;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding b;

    private FragmentManager fragmentManager;
    private User user;
    private ChatListAdapter chatListAdapter;
    private Disposable addChatDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = LiveData.getInstance().userMutableLiveData.getValue();
        fragmentManager = getSupportFragmentManager();
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        b = DataBindingUtil.setContentView(this, R.layout.activity_main);
        b.selectedUserId.setText(user.getId());
        b.selectedUserId.setOnClickListener(v -> {
            ClipboardService.setClipboard(this, user.getId());
            Toast.makeText(this, "UUID скопирован в буфер обмена", Toast.LENGTH_SHORT).show();
        });
        chatListAdapter = new ChatListAdapter(this, ChatService.findAll());
        b.chatList.setAdapter(chatListAdapter);
        addChatDisposable = ChatService.chatPublishSubject.subscribe(chat -> {
            chatListAdapter.add(chat);

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addChatDisposable.dispose();
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (data != null) {
//            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//            if (intentResult != null) {
//                QRResult qrResult = QRTools.parseQrResult(intentResult.getContents());
//                if (qrResult.getAesKey() == null) {
//                    //СКАНИРОВАНИЕ ID Пользователя
//                    Chat chat = LiveData.getInstance().emergingChat.getValue();
//                    chat.setUserId(qrResult.getUserId());
//                    ChatService.save(chat);
//                    getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).remove(fragmentManager.findFragmentByTag(Constants.AES_QR_FRAGMENT_TAG)).commit();
//
//                } else {
//                    //СКАНИРОВАНИЕ ID Пользователя И КЛЮЧА
//                    Chat chat = new Chat();
//                    chat.setUserId(qrResult.getUserId());
//                    chat.setAesKey(qrResult.getAesKey());
//                    fragmentManager.beginTransaction().add(R.id.main_activity_root_container, QrFragment.newInstance(false), Constants.AES_QR_FRAGMENT_TAG).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
//                    fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).remove(fragmentManager.findFragmentByTag(Constants.AES_KEYGEN_ROLE_FRAGMENT_TAG)).commit();
//                }
//            }
//        }
//
//    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getFragments().isEmpty()) {
            super.onBackPressed();
            return;
        }
        Fragment fragment;
        fragment = fragmentManager.findFragmentByTag(Constants.AES_QR_FRAGMENT_TAG);
        if (fragment != null) {
            ((QrFragment) fragment).back(null);
            return;
        }
        fragment = fragmentManager.findFragmentByTag(Constants.AES_KEYGEN_ROLE_FRAGMENT_TAG);
        if (fragment != null) {
            ((AesKeygenRoleFragment) fragment).back(null);
            return;
        }
        fragment = fragmentManager.findFragmentByTag(NEW_CONVERSATION_FRAGMENT_TAG);
        if (fragment != null) {
            fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).remove(fragment).commit();
        }


    }

    public void newConversation(View view) {
        if (fragmentManager.findFragmentByTag(NEW_CONVERSATION_FRAGMENT_TAG) == null)
            fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(R.id.main_activity_root_container, AddConversationFragment.newInstance(), NEW_CONVERSATION_FRAGMENT_TAG).commit();

    }
}