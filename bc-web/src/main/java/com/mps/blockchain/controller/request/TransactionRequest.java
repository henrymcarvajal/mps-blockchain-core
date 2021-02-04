package com.mps.blockchain.controller.request;

import java.math.BigInteger;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TransactionRequest {
	private UUID idSeller;
	private UUID idBuyer;
	private UUID idContract;
	@NotNull(message = "ContractName must be not null")
	@NotEmpty(message = "ContractName must be not empty")
	private String contractName;
	@NotNull(message = "Operation must be not null")
	@NotEmpty(message = "Operation must be not empty")
	private String operation;
	private BigInteger value;

	public UUID getIdSeller() {
		return idSeller;
	}

	public void setIdSeller(UUID idSeller) {
		this.idSeller = idSeller;
	}

	public UUID getIdBuyer() {
		return idBuyer;
	}

	public void setIdBuyer(UUID idBuyer) {
		this.idBuyer = idBuyer;
	}

	public UUID getIdContract() {
		return idContract;
	}

	public void setIdContract(UUID idContract) {
		this.idContract = idContract;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public BigInteger getValue() {
		return value;
	}

	public void setValue(BigInteger value) {
		this.value = value;
	}

}
