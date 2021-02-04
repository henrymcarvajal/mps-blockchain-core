package com.mps.blockchain.contracts.definitions.compraventa.operations.releasefunds;

import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.contracts.definitions.compraventa.operations.InputNames;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

class InputParameters {

	private UUID buyerId;
	private UUID contractId;

	public UUID getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(UUID buyerId) {
		this.buyerId = buyerId;
	}

	public UUID getContractId() {
		return contractId;
	}

	public void setContractId(UUID contractId) {
		this.contractId = contractId;
	}

	public static InputParameters build(Map<String, String> inputs) throws MissingInputException {
		if (!inputs.containsKey(InputNames.CONTRACT_BUYER)) {
			throw new MissingInputException(InputNames.CONTRACT_BUYER);
		}
		if (!inputs.containsKey(InputNames.CONTRACT_ID)) {
			throw new MissingInputException(InputNames.CONTRACT_ID);
		}

		InputParameters inputParameters = new InputParameters();
		inputParameters.setBuyerId(UUID.fromString(inputs.get(InputNames.CONTRACT_BUYER)));
		inputParameters.setContractId(UUID.fromString(inputs.get(InputNames.CONTRACT_ID)));

		return inputParameters;
	}

}
