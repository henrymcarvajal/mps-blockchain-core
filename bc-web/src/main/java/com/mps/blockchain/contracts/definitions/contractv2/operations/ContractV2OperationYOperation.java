package com.mps.blockchain.contracts.definitions.contractv2.operations;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.contracts.definitions.ContractOperation;

@Component
public class ContractV2OperationYOperation implements ContractOperation {
    
    @Override
    public String getOperationName() {
        return "OperationY";
    }
    
    @Override
    public void buildInputs(Map<String, String> inputs) {
        // Empty implementation
    }
    
    @Override
    public OperationResult execute(Map<String, String> outputs) {
        return OperationResult.SUCCESS;
    }
}
