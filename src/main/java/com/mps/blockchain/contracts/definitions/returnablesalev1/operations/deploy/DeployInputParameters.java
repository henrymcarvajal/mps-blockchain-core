package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.deploy;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.ReturnableSaleInputNames;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

class DeployInputParameters {

	private UUID sellerId;
	private UUID buyerId;
	private BigInteger contractValue;

	public UUID getSellerId() {
		return sellerId;
	}

	public void setSellerId(UUID sellerId) {
		this.sellerId = sellerId;
	}

	public UUID getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(UUID buyerId) {
		this.buyerId = buyerId;
	}

	public BigInteger getContractValue() {
		return contractValue;
	}

	public void setContractValue(BigInteger contractValue) {
		this.contractValue = contractValue;
	}

	public static DeployInputParameters build(Map<String, String> inputs) throws MissingInputException {
		if (!inputs.containsKey(ReturnableSaleInputNames.CONTRACT_SELLER)) {
			throw new MissingInputException(ReturnableSaleInputNames.CONTRACT_SELLER);
		}
		if (!inputs.containsKey(ReturnableSaleInputNames.CONTRACT_BUYER)) {
			throw new MissingInputException(ReturnableSaleInputNames.CONTRACT_BUYER);
		}
		if (!inputs.containsKey(ReturnableSaleInputNames.CONTRACT_VALUE)) {
			throw new MissingInputException(ReturnableSaleInputNames.CONTRACT_VALUE);
		}

		DeployInputParameters inputParameters = new DeployInputParameters();
		inputParameters.setSellerId(UUID.fromString(inputs.get(ReturnableSaleInputNames.CONTRACT_SELLER)));
		inputParameters.setBuyerId(UUID.fromString(inputs.get(ReturnableSaleInputNames.CONTRACT_BUYER)));
		inputParameters.setContractValue(new BigInteger(inputs.get(ReturnableSaleInputNames.CONTRACT_VALUE)));

		return inputParameters;
	}

}
