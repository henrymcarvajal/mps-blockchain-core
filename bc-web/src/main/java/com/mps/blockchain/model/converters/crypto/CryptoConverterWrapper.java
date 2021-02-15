package com.mps.blockchain.model.converters.crypto;

import java.nio.charset.StandardCharsets;

import com.mps.blockchain.security.EncryptorAesGcmPassword;

public class CryptoConverterWrapper {
    
    private CryptoConverterWrapper() {
        // empty implementation
    }
    
    public static String encrypt(String plainText, String key) {
        try {
            return EncryptorAesGcmPassword.encrypt(plainText.getBytes(StandardCharsets.UTF_8), key);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static String decrypt(String cryptoText, String key) {
        try {
            return EncryptorAesGcmPassword.decrypt(cryptoText, key);
        } catch (Exception ex) {
            return null;
        }
    }
    
}
