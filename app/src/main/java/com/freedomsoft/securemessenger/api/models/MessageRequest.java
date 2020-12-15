package com.freedomsoft.securemessenger.api.models;

import android.util.Base64;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest implements Serializable {
    private Long id;
    private String senderId;
    private String destinationId;
    private String message;
    private String iv;


    public static MessageRequest create(Message message) {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setSenderId(message.getSenderId());
        messageRequest.setIv(Base64.encodeToString(message.getIv(), Base64.DEFAULT));
        messageRequest.setDestinationId(message.getDestinationId());
        messageRequest.setMessage(Base64.encodeToString(message.getMessage(), Base64.DEFAULT));
        return messageRequest;
    }
}
