package com.freedomsoft.securemessenger;

import android.app.Application;

import androidx.room.Room;

import com.freedomsoft.securemessenger.api.MessengerApi;
import com.freedomsoft.securemessenger.api.StompApi;
import com.freedomsoft.securemessenger.room.AppDatabase;

public class App extends Application {
    public static AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        Typefaces.init(this);
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").allowMainThreadQueries().build();
        MessengerApi.init("http://winnersonx.ddns.net/");
//        MessengerApi.init("http://192.168.0.101:8080/");
        StompApi.init();
    }
}
