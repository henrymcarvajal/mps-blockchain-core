package com.mps.blockchain.persistence.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.model.BlockchainAccount;
import com.mps.blockchain.persistence.repository.BlockchainAccountRepository;

@Service
public class BlockchainAccountRepositoryService {
    
    @Autowired
    private BlockchainAccountRepository repository;
    
    public Optional<BlockchainAccount> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Illegal argument: id is null");
        }
        return repository.findById(id);
    }
    
    public void create(BlockchainAccount blockchainAccount) {
        if (blockchainAccount == null) {
            throw new IllegalArgumentException("Illegal argument: blockchainAccount is null");
        }
        blockchainAccount.setCreatedDate(LocalDateTime.now());
        repository.save(blockchainAccount);
    }
}
