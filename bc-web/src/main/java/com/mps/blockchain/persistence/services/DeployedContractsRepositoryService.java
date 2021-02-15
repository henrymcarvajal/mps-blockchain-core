package com.mps.blockchain.persistence.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.model.DeployedContract;
import com.mps.blockchain.persistence.repository.DeployedContractsRepository;

@Service
public class DeployedContractsRepositoryService {
    
    @Autowired
    private DeployedContractsRepository repository;
    
    public Optional<DeployedContract> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Illegal argument: id is null");
        }
        return repository.findById(id);
    }
    
    public void create(DeployedContract deployedContract) {
        if (deployedContract == null) {
            throw new IllegalArgumentException("Illegal argument: deployedContract is null");
        }
        
        deployedContract.setCreatedDate(LocalDateTime.now());
        repository.save(deployedContract);
    }
    
    public void update(DeployedContract deployedContract) {
        if (deployedContract == null) {
            throw new IllegalArgumentException("Illegal argument: deployedContract is null");
        }
        
        deployedContract.setModifiedDate(LocalDateTime.now());
        repository.save(deployedContract);
    }
    
}
