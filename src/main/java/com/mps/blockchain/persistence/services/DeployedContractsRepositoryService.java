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
		return repository.findById(id);
	}

	public void create(DeployedContract contract) {
		if (contract != null) {
			contract.setCreatedDate(LocalDateTime.now());
			repository.save(contract);
		}
	}
	
	public void update(DeployedContract contract) {
		if (contract != null) {
			contract.setModifiedDate(LocalDateTime.now());
			repository.save(contract);
		}
	}

}
