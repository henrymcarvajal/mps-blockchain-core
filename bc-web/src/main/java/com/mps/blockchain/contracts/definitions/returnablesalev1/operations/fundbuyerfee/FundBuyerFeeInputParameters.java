package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.fundbuyerfee;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.ReturnableSaleInputNames;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

class FundBuyerFeeInputParameters {

	private UUID contractId;
	private BigDecimal buyerFee;

	public UUID getContractId() {
		return contractId;
	}

	public void setContractId(UUID contractId) {
		this.contractId = contractId;
	}

	public BigDecimal getBuyerFee() {
		return buyerFee;
	}

	public void setBuyerFee(BigDecimal contractFee) {
		this.buyerFee = contractFee;
	}

	public static FundBuyerFeeInputParameters build(Map<String, String> inputs) throws MissingInputException {
		if (!inputs.containsKey(ReturnableSaleInputNames.CONTRACT_ID)) {
			throw new MissingInputException(ReturnableSaleInputNames.CONTRACT_ID);
		}
		if (!inputs.containsKey(ReturnableSaleInputNames.BUYER_FEE)) {
			throw new MissingInputException(ReturnableSaleInputNames.BUYER_FEE);
		}

		FundBuyerFeeInputParameters inputParameters = new FundBuyerFeeInputParameters();
		inputParameters.setContractId(UUID.fromString(inputs.get(ReturnableSaleInputNames.CONTRACT_ID)));
		inputParameters.setBuyerFee(new BigDecimal(inputs.get(ReturnableSaleInputNames.BUYER_FEE)));

		return inputParameters;
	}

}
