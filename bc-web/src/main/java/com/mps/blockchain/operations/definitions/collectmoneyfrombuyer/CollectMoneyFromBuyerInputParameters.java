package com.mps.blockchain.operations.definitions.collectmoneyfrombuyer;

import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.operations.definitions.OperationsInputNames;

class CollectMoneyFromBuyerInputParameters {
    
    private UUID buyerId;
    
    public UUID getBuyerId() {
        return buyerId;
    }
    
    public void setBuyerId(UUID buyerId) {
        this.buyerId = buyerId;
    }
    
    public static CollectMoneyFromBuyerInputParameters build(Map<String, String> inputs) throws MissingInputException {
        if (!inputs.containsKey(OperationsInputNames.OPERATION_BUYER)) {
            throw new MissingInputException(OperationsInputNames.OPERATION_BUYER);
        }
        
        CollectMoneyFromBuyerInputParameters inputParameters = new CollectMoneyFromBuyerInputParameters();
        inputParameters.setBuyerId(UUID.fromString(inputs.get(OperationsInputNames.OPERATION_BUYER)));
        
        return inputParameters;
    }
    
}
