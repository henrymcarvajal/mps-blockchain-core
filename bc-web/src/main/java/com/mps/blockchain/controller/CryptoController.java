package com.mps.blockchain.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mps.blockchain.security.DecryptionException;
import com.mps.blockchain.security.EncryptionException;
import com.mps.blockchain.security.EncryptorAesGcmPassword;

@RestController
@RequestMapping("/crypto/")
public class CryptoController {
    
    @GetMapping("/decrypt")
    public String decrypt(String cText, String password) throws DecryptionException {
        return EncryptorAesGcmPassword.decrypt(cText, password);
    }
    
    @GetMapping("/encrypt")
    public String encrypt(String pText, String password) throws EncryptionException {
        return EncryptorAesGcmPassword.encrypt(pText.getBytes(), password);
    }
}
