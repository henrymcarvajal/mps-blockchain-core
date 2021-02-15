package com.mps.blockchain.persistence.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.model.EnqueuedOperation;
import com.mps.blockchain.persistence.repository.EnqueuedOperationRepository;

@Service
public class EnqueuedOperationRepositoryService {
    
    @Autowired
    private EnqueuedOperationRepository repository;
    
    public Optional<EnqueuedOperation> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Illegal argument: id is null");
        }
        return repository.findById(id);
    }
    
    public void create(EnqueuedOperation queuedOperation) {
        if (queuedOperation == null) {
            throw new IllegalArgumentException("Illegal argument: queuedOperation is null");
        }
        queuedOperation.setCreatedDate(LocalDateTime.now());
        repository.save(queuedOperation);
    }
    
    public void update(EnqueuedOperation queuedOperation) {
        if (queuedOperation == null) {
            throw new IllegalArgumentException("Illegal argument: queuedOperation is null");
        }
        queuedOperation.setModifiedDate(LocalDateTime.now());
        repository.save(queuedOperation);
    }
}
