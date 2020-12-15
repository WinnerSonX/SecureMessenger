package com.freedomsoft.securemessenger.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.freedomsoft.securemessenger.room.dao.ChatRepo;
import com.freedomsoft.securemessenger.room.dao.UserRepo;
import com.freedomsoft.securemessenger.room.models.Chat;
import com.freedomsoft.securemessenger.room.models.User;

@Database(entities = {User.class, Chat.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserRepo userRepo();
    public abstract ChatRepo chatRepo();
}