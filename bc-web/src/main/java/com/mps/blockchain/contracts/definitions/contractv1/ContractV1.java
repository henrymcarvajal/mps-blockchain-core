package com.mps.blockchain.contracts.definitions.contractv1;

import org.springframework.stereotype.Component;

@Component
public class ContractV1 {

	public ContractV1() {
	}

	public String deploy(Object args) {
		return "ContractV1.sendBuyerDeposit() executed";
	}

	public String sendBuyerDeposit(Object args) {
		return "ContractV1.sendBuyerDeposit() executed";
	}

	public String sendSellerDeposit(Object args) {
		return "ContractV1.sendSellerDeposit() executed";
	}

	public String releaseFunds(Object args) {
		return "ContractV1.releaseFunds() executed";
	}
}
