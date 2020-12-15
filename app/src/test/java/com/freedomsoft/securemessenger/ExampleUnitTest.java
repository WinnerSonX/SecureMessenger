package com.freedomsoft.securemessenger;

import com.freedomsoft.securemessenger.security.Encryptor;
import com.freedomsoft.securemessenger.security.KeyGen;

import org.junit.Test;

import java.security.SecureRandom;
import java.util.Optional;

import javax.crypto.SecretKey;

import static org.junit.Assert.assertEquals;

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testCipher() {
        SecretKey secretKey = KeyGen.generateRandom();
        Encryptor encryptor = Encryptor.newInstance(secretKey.getEncoded());
        String helloString = "hello";
        byte[] iv = KeyGen.createIV(16, Optional.empty());
        byte[] encrypted = encryptor.encrypt(helloString,iv);
        String decrypted = encryptor.decrypt(encrypted,iv);
        assertEquals(helloString, decrypted);
    }
}