package com.mps.blockchain.contracts.definitions.contractv1.operations;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.OperationResult;

@Component
public class ContractV1SendSellerDepositOperation implements ContractOperation {

	private static final String OPERATION_NAME = "SendSellerDeposit";

	@Override
	public String getOperationName() {
		return OPERATION_NAME;
	}

	@Override
	public void buildInputs(Map<String, String> inputs) {
		// TODO Auto-generated method stub
	}

	@Override
	public OperationResult execute(Map<String, Object> outputs) {
		return OperationResult.SUCCESS;
	}
}