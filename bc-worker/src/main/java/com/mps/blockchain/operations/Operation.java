package com.mps.blockchain.operations;

import java.util.Map;

import com.mps.blockchain.commons.operations.OperationResult;

public interface Operation {
    
    String getName();
    
    void setInputs(Map<String, String> inputs);
    
    Map<String, String> getInputs();
    
    OperationResult execute(Map<String, String> outputs);
}
