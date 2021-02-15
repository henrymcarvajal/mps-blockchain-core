package com.mps.blockchain.model.converters.crypto;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.beans.factory.annotation.Autowired;

import com.mps.blockchain.config.BlockchainPasswords;

@Converter(autoApply = false)
public class JpaCryptoConverterForPasswords implements AttributeConverter<String, String> {
    
    private String cryptoKey;
    
    @Autowired
    public JpaCryptoConverterForPasswords(BlockchainPasswords blockChainPasswords) {
        cryptoKey = blockChainPasswords.forAccountPasswords();
    }
    
    @Override
    public final String convertToDatabaseColumn(String plainText) {
        return CryptoConverterWrapper.encrypt(plainText, cryptoKey);
    }
    
    @Override
    public final String convertToEntityAttribute(String cryptoText) {
        return CryptoConverterWrapper.decrypt(cryptoText, cryptoKey);
    }
}
