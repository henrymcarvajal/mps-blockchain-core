package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.deploy;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.ReturnableSaleInputNames;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

class DeployInputParameters {

    private UUID transactionId;
    private UUID sellerId;
    private UUID buyerId;
    private BigDecimal contractValue;

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

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

    public BigDecimal getContractValue() {
        return contractValue;
    }

    public void setContractValue(BigDecimal contractValue) {
        this.contractValue = contractValue;
    }

    public static DeployInputParameters build(Map<String, String> inputs) throws MissingInputException {
        if (!inputs.containsKey(ReturnableSaleInputNames.TRANSACTION_ID)) {
            throw new MissingInputException(ReturnableSaleInputNames.TRANSACTION_ID);
        }
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
        inputParameters.setTransactionId(UUID.fromString(inputs.get(ReturnableSaleInputNames.TRANSACTION_ID)));
        inputParameters.setSellerId(UUID.fromString(inputs.get(ReturnableSaleInputNames.CONTRACT_SELLER)));
        inputParameters.setBuyerId(UUID.fromString(inputs.get(ReturnableSaleInputNames.CONTRACT_BUYER)));
        inputParameters.setContractValue(new BigDecimal(inputs.get(ReturnableSaleInputNames.CONTRACT_VALUE)));

        return inputParameters;
    }

}
