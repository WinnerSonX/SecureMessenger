package com.freedomsoft.securemessenger.api;

import com.freedomsoft.securemessenger.api.models.Message;
import com.freedomsoft.securemessenger.api.models.MessageRequest;
import com.freedomsoft.securemessenger.room.models.Chat;
import com.freedomsoft.securemessenger.room.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessengerApi {
    private static final MessengerApi messengerApi = new MessengerApi();
    protected OkHttpClient client = new OkHttpClient.Builder().build();
    protected Retrofit retrofit;
    private MessageService messageService;

    public static void init(String baseUrl) {
        messengerApi.initApi(baseUrl);
    }

    private void initApi(String baseUrl) {
        retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client).build();
        messageService = retrofit.create(MessageService.class);
    }

    public static MessengerApi getInstance() {
        return messengerApi;
    }

    public Single<Response<ResponseBody>> sendMessage(Message message) {

        return messageService.send(MessageRequest.create(message))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Response<List<Message>>> getMessages(User user, Chat chat, Long afterId) {

        return messageService.get(chat.getUserId(), user.getId(), afterId)
                .map(listResponse -> {
                    List<Message> messages = new ArrayList<>();
                    if (listResponse.isSuccessful())
                        messages = listResponse.body().stream().map(Message::from).collect(Collectors.toList());
                    return Response.success(listResponse.code(), messages);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
