package com.freedomsoft.securemessenger.api.models;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message implements Serializable {
    private Long id;
    private String senderId;
    private String destinationId;
    private byte[] message;
    private byte[] iv;


    public static Message create(String senderId, String destinationId, byte[] encryptedMessage, byte[] iv) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setIv(iv);
        message.setDestinationId(destinationId);
        message.setMessage(encryptedMessage);
        return message;
    }

    public static Message from(MessageRequest messageRequest) {
        Message message = new Message();
        message.setId(messageRequest.getId());
        message.setSenderId(messageRequest.getSenderId());
        message.setDestinationId(messageRequest.getDestinationId());
        message.setIv(Base64.decode(messageRequest.getIv(), Base64.DEFAULT));
        message.setMessage(Base64.decode(messageRequest.getMessage(), Base64.DEFAULT));
        return message;
    }

    public static Message fromPayload(String payload) {
        Message message = new Message();
        try {
            JSONObject jsonObject = new JSONObject(payload);
            message.setId(jsonObject.getLong("id"));
            message.setSenderId(jsonObject.getString("senderId"));
            message.setDestinationId(jsonObject.getString("destinationId"));
            message.setIv(Base64.decode(jsonObject.getString("iv"), Base64.DEFAULT));
            message.setMessage(Base64.decode(jsonObject.getString("message"), Base64.DEFAULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    public String toPayload() {
        JSONObject payload = new JSONObject();
        try {
            payload.put("id", id);
            payload.put("senderId", senderId);
            payload.put("destinationId", destinationId);
            payload.put("message", Base64.encodeToString(message, Base64.DEFAULT));
            payload.put("iv", Base64.encodeToString(iv, Base64.DEFAULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload.toString();
    }
}
