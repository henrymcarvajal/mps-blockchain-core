package com.mps.blockchain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "buyer_account")
public class BuyerAccount {

	@Id
	@GeneratedValue
	private UUID id;
	@Column(name = "mps_buyer_id")
	private UUID mpsBuyerId;
	@Column(name = "blockchain_account_id")
	private UUID blockchainAccountId;
	
	@Column(name = "created_date", nullable = false, updatable = false)
	private LocalDateTime createdDate;
	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

	public BuyerAccount() {
		this.createdDate = LocalDateTime.now();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getMpsBuyerId() {
		return mpsBuyerId;
	}

	public void setMpsBuyerId(UUID mpsBuyerId) {
		this.mpsBuyerId = mpsBuyerId;
	}

	public UUID getBlockchainAccountId() {
		return blockchainAccountId;
	}

	public void setBlockchainAccountId(UUID blockchainAccountId) {
		this.blockchainAccountId = blockchainAccountId;
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

	@Override
	public String toString() {
		return "BuyerAccount [id=" + id + ", idBuyer=" + mpsBuyerId + ", idAccount=" + blockchainAccountId + ", createdDate="
				+ createdDate + ", modifiedDate=" + modifiedDate + "]";
	}

}
