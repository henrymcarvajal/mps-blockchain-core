package com.mps.blockchain.persistence.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.model.Transaction;
import com.mps.blockchain.persistence.repository.TransactionRepository;

@Service
public class TransactionRepositoryService {
    
    @Autowired
    private TransactionRepository repository;
    
    public Optional<Transaction> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Illegal argument: id is null");
        }
        return repository.findById(id);
    }
    
    public void create(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Illegal argument: transaction is null");
        }
        transaction.setCreatedDate(LocalDateTime.now());
        repository.save(transaction);
    }
    
    public void update(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Illegal argument: transaction is null");
        }
        transaction.setModifiedDate(LocalDateTime.now());
        repository.save(transaction);
    }
}
