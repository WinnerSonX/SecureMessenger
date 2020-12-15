package com.freedomsoft.securemessenger.security;

import java.nio.charset.StandardCharsets;

import okhttp3.RequestBody;

public class Converter {
    public static byte[] toBytes(String string) {
        return string.getBytes(StandardCharsets.UTF_8);

    }

    public static String fromBytes(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
