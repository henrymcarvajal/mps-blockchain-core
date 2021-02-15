package com.mps.blockchain.contracts.definitions.contractv2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ContractV2 {
    
    public static final String TYPE_NAME = "ContractV2";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractV2.class);
    
    public void deploy(Object args) {
        LOGGER.info("ContractV2.deploy() executed with args: {}", args);
    }
    
    public void operationX(Object args) {
        LOGGER.info("ContractV2.operationX() executed with args: {}", args);
    }
    
    public void operationY(Object args) {
        LOGGER.info("ContractV2.OperationY() executed with args: {}", args);
    }
}
