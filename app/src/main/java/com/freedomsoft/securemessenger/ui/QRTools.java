package com.freedomsoft.securemessenger.ui;

import android.util.Base64;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class QRTools {
    public static String encodeQrJson(String userId, @Nullable byte[] aesKey) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", userId);
            if (aesKey != null)
                jsonObject.put("aes_key", Base64.encodeToString(aesKey,Base64.DEFAULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static QRResult parseQrResult(String qrJson) {
        QRResult qrResult = new QRResult();
        try {
            JSONObject jsonObject = new JSONObject(qrJson);
            qrResult.setUserId(jsonObject.getString("user_id"));
            if (jsonObject.has("aes_key"))
                qrResult.setAesKey(Base64.decode(jsonObject.getString("aes_key"),Base64.DEFAULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return qrResult;
    }

}
