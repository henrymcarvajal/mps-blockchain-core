package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.setdisputewinner;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.ReturnableSaleInputNames;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

class SetDisputeWinnerInputParameters {

	private UUID contractId;
	private BigInteger disputeWinner;
	private BigInteger buyerCharges;
	private BigInteger sellerCharges;

	public UUID getContractId() {
		return contractId;
	}

	public void setContractId(UUID contractId) {
		this.contractId = contractId;
	}

	public BigInteger getDisputeWinner() {
		return disputeWinner;
	}

	public void setDisputeWinner(BigInteger winner) {
		this.disputeWinner = winner;
	}

	public BigInteger getBuyerCharges() {
		return buyerCharges;
	}

	public void setBuyerCharges(BigInteger buyerCharges) {
		this.buyerCharges = buyerCharges;
	}

	public BigInteger getSellerCharges() {
		return sellerCharges;
	}

	public void setSellerCharges(BigInteger sellerCharges) {
		this.sellerCharges = sellerCharges;
	}

	public static SetDisputeWinnerInputParameters build(Map<String, String> inputs) throws MissingInputException {
		if (!inputs.containsKey(ReturnableSaleInputNames.CONTRACT_ID)) {
			throw new MissingInputException(ReturnableSaleInputNames.CONTRACT_ID);
		}
		if (!inputs.containsKey(ReturnableSaleInputNames.DISPUTE_WINNER)) {
			throw new MissingInputException(ReturnableSaleInputNames.DISPUTE_WINNER);
		}
		if (!inputs.containsKey(ReturnableSaleInputNames.SELLER_CHARGES)) {
			throw new MissingInputException(ReturnableSaleInputNames.SELLER_CHARGES);
		}
		if (!inputs.containsKey(ReturnableSaleInputNames.BUYER_CHARGES)) {
			throw new MissingInputException(ReturnableSaleInputNames.BUYER_CHARGES);
		}

		SetDisputeWinnerInputParameters inputParameters = new SetDisputeWinnerInputParameters();
		inputParameters.setContractId(UUID.fromString(inputs.get(ReturnableSaleInputNames.CONTRACT_ID)));
		inputParameters.setDisputeWinner(new BigInteger(inputs.get(ReturnableSaleInputNames.DISPUTE_WINNER)));
		inputParameters.setBuyerCharges(new BigInteger(inputs.get(ReturnableSaleInputNames.SELLER_CHARGES)));
		inputParameters.setSellerCharges(new BigInteger(inputs.get(ReturnableSaleInputNames.BUYER_CHARGES)));

		return inputParameters;
	}

}
