package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.fundsellerfee;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.ReturnableSaleInputNames;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

class FundSellerFeeInputParameters {

	private UUID contractId;
	private BigDecimal sellerFee;

	public UUID getContractId() {
		return contractId;
	}

	public void setContractId(UUID contractId) {
		this.contractId = contractId;
	}

	public BigDecimal getSellerFee() {
		return sellerFee;
	}

	public void setSellerFee(BigDecimal contractFee) {
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
		inputParameters.setSellerFee(new BigDecimal(inputs.get(ReturnableSaleInputNames.SELLER_FEE)));

		return inputParameters;
	}

}
