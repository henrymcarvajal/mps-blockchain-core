package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.payseller;

import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.ReturnableSaleInputNames;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

class PaySellerInputParameters {
    
    private UUID sellerId;
    private UUID contractId;
    
    public UUID getSellerId() {
        return sellerId;
    }
    
    public void setSellerId(UUID sellerId) {
        this.sellerId = sellerId;
    }
    
    public UUID getContractId() {
        return contractId;
    }
    
    public void setContractId(UUID contractId) {
        this.contractId = contractId;
    }
    
    public static PaySellerInputParameters build(Map<String, String> inputs) throws MissingInputException {
        if (!inputs.containsKey(ReturnableSaleInputNames.CONTRACT_SELLER)) {
            throw new MissingInputException(ReturnableSaleInputNames.CONTRACT_SELLER);
        }
        if (!inputs.containsKey(ReturnableSaleInputNames.CONTRACT_ID)) {
            throw new MissingInputException(ReturnableSaleInputNames.CONTRACT_ID);
        }
        
        PaySellerInputParameters inputParameters = new PaySellerInputParameters();
        inputParameters.setSellerId(UUID.fromString(inputs.get(ReturnableSaleInputNames.CONTRACT_SELLER)));
        inputParameters.setContractId(UUID.fromString(inputs.get(ReturnableSaleInputNames.CONTRACT_ID)));
        
        return inputParameters;
    }
    
}
