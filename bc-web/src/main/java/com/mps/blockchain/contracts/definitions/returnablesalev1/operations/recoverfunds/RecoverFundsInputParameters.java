package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.recoverfunds;

import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.ReturnableSaleInputNames;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

class RecoverFundsInputParameters {

	private UUID contractId;

	public UUID getContractId() {
		return contractId;
	}

	public void setContractId(UUID contractId) {
		this.contractId = contractId;
	}

	public static RecoverFundsInputParameters build(Map<String, String> inputs) throws MissingInputException {
		if (!inputs.containsKey(ReturnableSaleInputNames.CONTRACT_ID)) {
			throw new MissingInputException(ReturnableSaleInputNames.CONTRACT_ID);
		}

		RecoverFundsInputParameters inputParameters = new RecoverFundsInputParameters();
		inputParameters.setContractId(UUID.fromString(inputs.get(ReturnableSaleInputNames.CONTRACT_ID)));

		return inputParameters;
	}

}
