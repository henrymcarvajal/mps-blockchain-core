package com.mps.blockchain.contracts.definitions.compraventa.operations.deploy;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.contracts.definitions.compraventa.operations.InputNames;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.BuyerAccount;
import com.mps.blockchain.model.SellerAccount;

class InputParameters {

	private UUID sellerId;
	private UUID buyerId;
	private SellerAccount sellerAccount;
	private BuyerAccount buyerAccount;
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

	public SellerAccount getSellerAccount() {
		return sellerAccount;
	}

	public void setSellerAccount(SellerAccount sellerAccount) {
		this.sellerAccount = sellerAccount;
	}

	public BuyerAccount getBuyerAccount() {
		return buyerAccount;
	}

	public void setBuyerAccount(BuyerAccount buyerAccount) {
		this.buyerAccount = buyerAccount;
	}

	public BigInteger getContractValue() {
		return contractValue;
	}

	public void setContractValue(BigInteger contractValue) {
		this.contractValue = contractValue;
	}

	public static InputParameters build(Map<String, String> inputs) throws MissingInputException {
		if (!inputs.containsKey(InputNames.CONTRACT_SELLER)) {
			throw new MissingInputException(InputNames.CONTRACT_SELLER);
		}
		if (!inputs.containsKey(InputNames.CONTRACT_BUYER)) {
			throw new MissingInputException(InputNames.CONTRACT_BUYER);
		}
		if (!inputs.containsKey(InputNames.CONTRACT_VALUE)) {
			throw new MissingInputException(InputNames.CONTRACT_VALUE);
		}

		InputParameters inputParameters = new InputParameters();
		inputParameters.setSellerId(UUID.fromString(inputs.get(InputNames.CONTRACT_SELLER)));
		inputParameters.setBuyerId(UUID.fromString(inputs.get(InputNames.CONTRACT_BUYER)));
		inputParameters.setContractValue(new BigInteger(inputs.get(InputNames.CONTRACT_VALUE)));

		return inputParameters;
	}

}
