package com.mps.blockchain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class DecryptedBlockchainAccount {

	private UUID id;
	private String address;
	private String password;
	private String seedPhrase;
	private String privateKey;
	private String publicKey;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	public DecryptedBlockchainAccount() {
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSeedPhrase() {
		return seedPhrase;
	}

	public void setSeedPhrase(String passPhrase) {
		this.seedPhrase = passPhrase;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
