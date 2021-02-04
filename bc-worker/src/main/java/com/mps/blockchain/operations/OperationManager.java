package com.mps.blockchain.operations;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OperationManager {
    
    private Map<String, Operation> availableOperations;
    
    public OperationManager() {
        availableOperations = new HashMap<>();
    }
    
    public void loadOperations() {
        OperationScanner scanner = new OperationScanner();
        scanner.scanOperations().forEach(name -> {
            Operation blockchainOperation = instantiate(name, Operation.class);
            availableOperations.put(blockchainOperation.getName(), blockchainOperation);
        });
    }
    
    public Optional<Operation> getOperation(String operationName) {
        if (availableOperations.containsKey(operationName)) {
            return Optional.of(availableOperations.get(operationName));
        }
        return Optional.empty();
    }
    
    public <T> T instantiate(final String className, final Class<T> type) {
        try {
            return type.cast(Class.forName(className).getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}