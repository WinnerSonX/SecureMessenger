package com.freedomsoft.securemessenger.room.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.freedomsoft.securemessenger.room.models.User;
@Dao
public interface UserRepo {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(User user);

    @Delete
    void delete(User user);

    @Update
    void update(User user);

    @Query("SELECT count(id)FROM users")
    long count();

    @Nullable
    @Query("SELECT * FROM users WHERE selected=1 LIMIT 1")
    User getSelected();

    @Query("SELECT id FROM users WHERE id=:id")
    long exists(String id);

    @Query("UPDATE users SET selected=0 WHERE selected=1")
    void removeSelection();

    @Query("UPDATE users SET selected=1 WHERE id =:id")
    void setSelected(String id);
}
