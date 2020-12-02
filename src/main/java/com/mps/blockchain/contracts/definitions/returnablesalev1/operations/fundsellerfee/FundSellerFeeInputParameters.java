package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.fundsellerfee;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.ReturnableSaleInputNames;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

class FundSellerFeeInputParameters {

	private UUID contractId;
	private BigInteger sellerFee;

	public UUID getContractId() {
		return contractId;
	}

	public void setContractId(UUID contractId) {
		this.contractId = contractId;
	}

	public BigInteger getSellerFee() {
		return sellerFee;
	}

	public void setSellerFee(BigInteger contractFee) {
		this.sellerFee = contractFee;
	}

	public static FundSellerFeeInputParameters build(Map<String, String> inputs) throws MissingInputException {
		if (!inputs.containsKey(ReturnableSaleInputNames.CONTRACT_ID)) {
			throw new MissingInputException(ReturnableSaleInputNames.CONTRACT_ID);
		}
		if (!inputs.containsKey(ReturnableSaleInputNames.SELLER_FEE)) {
			throw new MissingInputException(ReturnableSaleInputNames.SELLER_FEE);
		}

		FundSellerFeeInputParameters inputParameters = new FundSellerFeeInputParameters();
		inputParameters.setContractId(UUID.fromString(inputs.get(ReturnableSaleInputNames.CONTRACT_ID)));
		inputParameters.setSellerFee(new BigInteger(inputs.get(ReturnableSaleInputNames.SELLER_FEE)));

		return inputParameters;
	}

}
