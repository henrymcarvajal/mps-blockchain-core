package com.mps.blockchain.operations.definitions.collectmoneyfromseller;

import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.operations.definitions.OperationsInputNames;

class CollectMoneyFromSellerInputParameters {

	private UUID sellerId;

	public UUID getSellerId() {
		return sellerId;
	}

	public void setSellerId(UUID sellerId) {
		this.sellerId = sellerId;
	}

	public static CollectMoneyFromSellerInputParameters build(Map<String, String> inputs) throws MissingInputException {
		if (!inputs.containsKey(OperationsInputNames.OPERATION_SELLER)) {
			throw new MissingInputException(OperationsInputNames.OPERATION_SELLER);
		}

		CollectMoneyFromSellerInputParameters inputParameters = new CollectMoneyFromSellerInputParameters();
		inputParameters.setSellerId(UUID.fromString(inputs.get(OperationsInputNames.OPERATION_SELLER)));

		return inputParameters;
	}

}
