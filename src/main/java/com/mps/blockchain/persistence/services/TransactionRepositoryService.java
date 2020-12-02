package com.mps.blockchain.persistence.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.model.Transaction;
import com.mps.blockchain.persistence.repository.TransactionRepository;

@Service
public class TransactionRepositoryService {

	@Autowired
	private TransactionRepository repository;

	public void create(Transaction transaction) {
		if (transaction != null) {
			transaction.setCreatedDate(LocalDateTime.now());
			repository.save(transaction);
		}
	}
	
	public void update(Transaction transaction) {
		if (transaction != null) {
			transaction.setModifiedDate(LocalDateTime.now());
			repository.save(transaction);
		}
	}

}
