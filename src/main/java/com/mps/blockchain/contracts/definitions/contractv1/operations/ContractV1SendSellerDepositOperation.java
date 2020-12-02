package com.mps.blockchain.contracts.definitions.contractv1.operations;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.mps.blockchain.contracts.definitions.ContractOperation;

@Component
public class ContractV1SendSellerDepositOperation implements ContractOperation {

	private static String OPERATION_NAME = "SendSellerDeposit";

	@Override
	public String getOperationName() {
		return OPERATION_NAME;
	}

	@Override
	public void buildInputs(Map<String, String> inputs) {
		// TODO Auto-generated method stub
	}

	@Override
	public void execute(Map<String, Object> outputs) {
		// TODO Auto-generated method stub
	}
}