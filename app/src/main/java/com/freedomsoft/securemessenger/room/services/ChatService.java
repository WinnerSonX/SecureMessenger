package com.freedomsoft.securemessenger.room.services;

import com.freedomsoft.securemessenger.App;
import com.freedomsoft.securemessenger.room.dao.ChatRepo;
import com.freedomsoft.securemessenger.room.models.Chat;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

public class ChatService {
    public static PublishSubject<Chat> chatPublishSubject = PublishSubject.create();
    private static final ChatRepo chatRepo = App.appDatabase.chatRepo();

    public static void save(Chat chat) {
        chatRepo.save(chat);
        chatPublishSubject.onNext(chat);
    }

    public static List<Chat> findAll() {
        return chatRepo.findAll();
    }
}
