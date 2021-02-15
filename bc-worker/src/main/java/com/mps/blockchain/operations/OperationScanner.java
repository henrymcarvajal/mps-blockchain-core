package com.mps.blockchain.operations;

import java.util.List;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public class OperationScanner {
    
    private static final String BLOCKCHAIN_OPERATIONS_PACKAGE = "com.mps.blockchain.operations.implementations";
    
    public List<String> scanOperations() {
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistPackages(BLOCKCHAIN_OPERATIONS_PACKAGE)
                .scan()) {
            
            ClassInfoList widgetClasses = scanResult.getClassesImplementing(Operation.class.getName());
            return widgetClasses.getNames();
        }
    }
}
