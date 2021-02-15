package com.mps.blockchain.contracts.definitions.contractv2;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.OperationInvoker;
import com.mps.blockchain.contracts.definitions.contractv2.operations.ContractV2OperationXOperation;
import com.mps.blockchain.contracts.definitions.contractv2.operations.ContractV2OperationYOperation;

@Service
public class ContractV2OperationInvoker implements OperationInvoker {
    
    @Autowired
    private ContractV2OperationXOperation operationX;
    
    @Autowired
    private ContractV2OperationYOperation operationY;
    
    private Map<String, ContractOperation> availableOperations = new HashMap<>();
    private String currentOperation = null;
    
    @PostConstruct
    private void registerOperations() {
        this.availableOperations.put(operationX.getOperationName(), operationX);
        this.availableOperations.put(operationY.getOperationName(), operationY);
    }
    
    @Override
    public void buildInputs(String operationName, Map<String, String> inputs) {
        ContractOperation operation = this.availableOperations.get(operationName);
        if (operation == null) {
            throw new IllegalArgumentException("Operation '" + operationName + "' not available");
        }
        currentOperation = operationName;
    }
    
    @Override
    public OperationResult execute(Map<String, String> outputs) {
        ContractOperation operation = this.availableOperations.get(currentOperation);
        return operation.execute(outputs);
    }
    
    @Override
    public boolean supportsOperation(String operationName) {
        return this.availableOperations.containsKey(operationName);
    }
}
