package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.setdisputeonphase1;

import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.ReturnableSaleInputNames;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

class SetDisputeOnPhase1InputParameters {
    
    private UUID contractId;
    
    public UUID getContractId() {
        return contractId;
    }
    
    public void setContractId(UUID contractId) {
        this.contractId = contractId;
    }
    
    public static SetDisputeOnPhase1InputParameters build(Map<String, String> inputs) throws MissingInputException {
        if (!inputs.containsKey(ReturnableSaleInputNames.CONTRACT_ID)) {
            throw new MissingInputException(ReturnableSaleInputNames.CONTRACT_ID);
        }
        
        SetDisputeOnPhase1InputParameters inputParameters = new SetDisputeOnPhase1InputParameters();
        inputParameters.setContractId(UUID.fromString(inputs.get(ReturnableSaleInputNames.CONTRACT_ID)));
        
        return inputParameters;
    }
    
}
