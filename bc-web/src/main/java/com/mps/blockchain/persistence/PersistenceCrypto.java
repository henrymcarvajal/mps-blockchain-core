package com.mps.blockchain.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.config.BlockchainPasswords;

@Service
public class PersistenceCrypto {
    
    @Autowired
    private BlockchainPasswords blockChainPasswords;
    
    public void printValues() {
        blockChainPasswords.forAccountAddresses();
    }
}
