package com.mps.blockchain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "deployed_contracts")
public class DeployedContract {
	@Id
	@GeneratedValue
	private UUID id;
	private String address;
	@Column(name = "seller_account_id")
	private UUID sellerAccountId;
	@Column(name = "buyer_account_id")
	private UUID buyerAccountId;
	@Column(name = "configured_contract_id")
	private UUID configuredContractId;
	@Column(name = "receipt")
	private String receipt;
	@Column(name = "deployment_result")
	private String deploymentResult;
	
	@Column(name = "created_date", nullable = false, updatable = false)
	private LocalDateTime createdDate;
	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

	public DeployedContract() {
		this.createdDate = LocalDateTime.now();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public UUID getSellerAccountId() {
		return sellerAccountId;
	}

	public void setSellerAccountId(UUID sellerAccountId) {
		this.sellerAccountId = sellerAccountId;
	}

	public UUID getBuyerAccountId() {
		return buyerAccountId;
	}

	public void setBuyerAccountId(UUID buyerAccountId) {
		this.buyerAccountId = buyerAccountId;
	}

	public UUID getConfiguredContractId() {
		return configuredContractId;
	}

	public void setIdContract(UUID configuredContractId) {
		this.configuredContractId = configuredContractId;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getDeploymentResult() {
		return deploymentResult;
	}

	public void setDeploymentResult(String deploymentResult) {
		this.deploymentResult = deploymentResult;
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
