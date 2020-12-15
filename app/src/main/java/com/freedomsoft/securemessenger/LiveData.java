package com.freedomsoft.securemessenger;

import androidx.lifecycle.MutableLiveData;

import com.freedomsoft.securemessenger.room.models.Chat;
import com.freedomsoft.securemessenger.room.models.User;

import java.util.Optional;

public class LiveData {
    private static final LiveData liveData = new LiveData();

    public static LiveData getInstance() {
        return liveData;
    }

    private LiveData() {
    }

    public MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Chat> emergingChat = new MutableLiveData<>();
    public MutableLiveData<Chat> currentChat = new MutableLiveData<>();
    public MutableLiveData<Optional<byte[]>> aesKeyMutableLiveData = new MutableLiveData<>();
}
