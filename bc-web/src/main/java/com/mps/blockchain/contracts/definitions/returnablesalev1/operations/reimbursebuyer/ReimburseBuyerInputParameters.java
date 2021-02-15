package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.reimbursebuyer;

import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.ReturnableSaleInputNames;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

class ReimburseBuyerInputParameters {
    
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
    
    public static ReimburseBuyerInputParameters build(Map<String, String> inputs) throws MissingInputException {
        if (!inputs.containsKey(ReturnableSaleInputNames.CONTRACT_BUYER)) {
            throw new MissingInputException(ReturnableSaleInputNames.CONTRACT_BUYER);
        }
        if (!inputs.containsKey(ReturnableSaleInputNames.CONTRACT_ID)) {
            throw new MissingInputException(ReturnableSaleInputNames.CONTRACT_ID);
        }
        
        ReimburseBuyerInputParameters inputParameters = new ReimburseBuyerInputParameters();
        inputParameters.setContractId(UUID.fromString(inputs.get(ReturnableSaleInputNames.CONTRACT_ID)));
        inputParameters.setBuyerId(UUID.fromString(inputs.get(ReturnableSaleInputNames.CONTRACT_BUYER)));
        
        return inputParameters;
    }
    
}
