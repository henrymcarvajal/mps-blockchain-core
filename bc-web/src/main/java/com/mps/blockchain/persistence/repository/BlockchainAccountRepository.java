package com.mps.blockchain.persistence.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mps.blockchain.model.BlockchainAccount;

@Repository
public interface BlockchainAccountRepository extends CrudRepository<BlockchainAccount, UUID> {
    
}
