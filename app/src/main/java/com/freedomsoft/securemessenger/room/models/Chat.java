package com.freedomsoft.securemessenger.room.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity(tableName = "chats")
public class Chat {
    @PrimaryKey
    @NonNull
    private String id = UUID.randomUUID().toString();
    @ColumnInfo(name = "user_id")
    private String userId;
    @ColumnInfo(name = "display_name")
    private String displayName;
    @ColumnInfo(name = "aes_key",typeAffinity = ColumnInfo.BLOB)
    private byte[] aesKey;
    @ColumnInfo(name = "last_message",typeAffinity = ColumnInfo.BLOB)
    private byte[] lastMessage;
    @ColumnInfo(name = "last_iv",typeAffinity = ColumnInfo.BLOB)
    private byte[] lasIv;
}
