package com.mps.blockchain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue
	private UUID id;
	@Column(name = "mps_transaction_id")
	private UUID mpsTransactionId;
	private String inputs;
	private String outputs;
	@Column(name = "transaction_result")
	private String result;
	
	@Column(name = "created_date", nullable = false, updatable = false)
	private LocalDateTime createdDate;
	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

	public Transaction() {
		this.createdDate = LocalDateTime.now();
	}

	public UUID getId() {
		return mpsTransactionId;
	}

	public void setId(UUID id) {
		this.id = id;
	}	
	
	public UUID getMpsTransactionId() {
		return mpsTransactionId;
	}

	public void setMpsTransactionId(UUID idTransaction) {
		this.mpsTransactionId = idTransaction;
	}

	public String getInputs() {
		return inputs;
	}

	public void setInputs(String inputs) {
		this.inputs = inputs;
	}

	public String getOutputs() {
		return outputs;
	}

	public void setOutputs(String outputs) {
		this.outputs = outputs;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		if (this.createdDate == null) {
			this.createdDate = createdDate;
		}
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
