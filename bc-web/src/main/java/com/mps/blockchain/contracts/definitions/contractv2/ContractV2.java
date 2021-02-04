package com.mps.blockchain.contracts.definitions.contractv2;

import org.springframework.stereotype.Component;

@Component
public class ContractV2 {

	public static String TYPE_NAME = "ContractV2";

	public void deploy(Object args) {
		System.out.println("ContractV2.deploy() executed");
	}

	public void operationX(Object args) {
		System.out.println("ContractV2.operationX() executed");
	}

	public void operationY(Object args) {
		System.out.println("ContractV2.OperationY() executed");
	}
}
