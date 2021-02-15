package com.mps.blockchain.contracts.definitions.contractv1;

import org.springframework.stereotype.Component;

@Component
public class ContractV1 {
    
    public ContractV1() {
        // Default no-args constructor
    }
    
    public String deploy(Object args) {
        return "ContractV1.sendBuyerDeposit() executed with args: " + args.toString();
    }
    
    public String sendBuyerDeposit(Object args) {
        return "ContractV1.sendBuyerDeposit() executed with args: " + args.toString();
    }
    
    public String sendSellerDeposit(Object args) {
        return "ContractV1.sendSellerDeposit() executed with args: " + args.toString();
    }
    
    public String releaseFunds(Object args) {
        return "ContractV1.releaseFunds() executed with args: " + args.toString();
    }
}
