package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.getbuyer;

import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.ReturnableSaleInputNames;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

class GetBuyerInputParameters {

	private UUID contractId;

	public UUID getContractId() {
		return contractId;
	}

	public void setContractId(UUID contractId) {
		this.contractId = contractId;
	}

	public static GetBuyerInputParameters build(Map<String, String> inputs) throws MissingInputException {
		if (!inputs.containsKey(ReturnableSaleInputNames.CONTRACT_ID)) {
			throw new MissingInputException(ReturnableSaleInputNames.CONTRACT_ID);
		}

		GetBuyerInputParameters inputParameters = new GetBuyerInputParameters();
		inputParameters.setContractId(UUID.fromString(inputs.get(ReturnableSaleInputNames.CONTRACT_ID)));

		return inputParameters;
	}

}
