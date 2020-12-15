package com.freedomsoft.securemessenger.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.freedomsoft.securemessenger.room.models.Chat;
import com.freedomsoft.securemessenger.room.models.User;

import java.util.List;

@Dao
public interface ChatRepo {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Chat user);

    @Delete
    void delete(Chat user);

    @Update
    void update(Chat user);

    @Query("SELECT * FROM chats")
    List<Chat> findAll();
}
