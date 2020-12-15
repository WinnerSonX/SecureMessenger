package com.freedomsoft.securemessenger.security;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
    private Cipher cipher;


    public void setSecretKey(byte[] secretKey) {
        this.secretKey = new SecretKeySpec(secretKey, "AES");
    }

    private SecretKey secretKey;

    public static Encryptor newInstance() {
        Encryptor encryptor = new Encryptor();
        try {
            encryptor.cipher = Cipher.getInstance("AES/CTR/PKCS5PADDING");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptor;
    }

    public static Encryptor newInstance(byte[] key) {
        Encryptor encryptor = new Encryptor();
        try {
            encryptor.secretKey = new SecretKeySpec(key, "AES");
            encryptor.cipher = Cipher.getInstance("AES/CTR/PKCS5PADDING");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptor;
    }

    public byte[] encrypt(String text, byte[] iv) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        try {
            return cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String bytes, byte[] iv) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        try {
            return new String(cipher.doFinal(Converter.toBytes(bytes)), StandardCharsets.UTF_8);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String decrypt(String bytes, String iv) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(Converter.toBytes(iv));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        try {
            return new String(cipher.doFinal(Converter.toBytes(bytes)), StandardCharsets.UTF_8);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String decrypt(byte[] bytes, byte[] iv) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        try {
            return new String(cipher.doFinal(bytes), StandardCharsets.UTF_8);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }
}
