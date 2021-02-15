package com.mps.blockchain.contracts.definitions.contractv2.operations;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.contracts.definitions.ContractOperation;

@Component
public class ContractV2OperationXOperation implements ContractOperation {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractV2OperationXOperation.class);
    
    @Override
    public String getOperationName() {
        return "OperationX";
    }
    
    @Override
    public void buildInputs(Map<String, String> inputs) {
        LOGGER.info("Building inputs from: {}", inputs);
    }
    
    @Override
    public OperationResult execute(Map<String, String> outputs) {
        return OperationResult.SUCCESS;
    }
}
