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
	private UUID idTransaction;
	private String inputs;
	private String outputs;

	@Column(name = "created_date", nullable = false, updatable = false)
	private LocalDateTime createdDate;
	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

	public Transaction() {
	}

	public UUID getId() {
		return idTransaction;
	}

	public void setId(UUID id) {
		this.id = id;
	}	
	
	public UUID getIdTransaction() {
		return idTransaction;
	}

	public void setIdTransaction(UUID idTransaction) {
		this.idTransaction = idTransaction;
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
