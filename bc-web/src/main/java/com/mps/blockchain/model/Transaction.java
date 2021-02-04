package com.mps.blockchain.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.mps.blockchain.model.converters.JpaConverterMap;

@Entity
public class Transaction {

	@Id
	@GeneratedValue
	private UUID id;
	@Column(name = "mps_transaction_id")
	private UUID mpsTransactionId;
	@Convert(converter = JpaConverterMap.class)
	private Map<String, String> inputs;
	@Convert(converter = JpaConverterMap.class)
	private Map<String, String> outputs;
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
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}	
	
	public UUID getMpsTransactionId() {
		return mpsTransactionId;
	}

	public void setMpsTransactionId(UUID mpsTransactionId) {
		this.mpsTransactionId = mpsTransactionId;
	}

	public Map<String, String> getInputs() {
		return inputs;
	}

	public void setInputs(Map<String, String> inputs) {
		this.inputs = inputs;
	}

	public Map<String, String> getOutputs() {
		return outputs;
	}

	public void setOutputs(Map<String, String> outputs) {
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
