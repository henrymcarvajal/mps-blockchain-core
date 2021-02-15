package com.mps.blockchain.operations;

import java.util.Map;

import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

public interface Operation {
    
    String getOperationName();
    
    void buildInputs(Map<String, String> inputs) throws MissingInputException;
    
    OperationResult execute(Map<String, String> outputs);
}
