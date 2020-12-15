package com.freedomsoft.securemessenger.room.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "users")
@Getter
@Setter
public class User {
    @PrimaryKey
    @NonNull
    private String id = UUID.randomUUID().toString();
    @ColumnInfo(name = "username")
    private String userName;
    @ColumnInfo(name = "selected")
    private boolean selected;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
