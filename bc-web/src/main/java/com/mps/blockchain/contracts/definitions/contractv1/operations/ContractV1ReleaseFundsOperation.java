package com.mps.blockchain.contracts.definitions.contractv1.operations;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.commons.operations.OperationResult;

@Component
public class ContractV1ReleaseFundsOperation implements ContractOperation {

	private static final String OPERATION_NAME = "ReleaseFunds";
	
	@Override
	public String getOperationName() {
		return OPERATION_NAME;
	}

	@Override
	public void buildInputs(Map<String, String> inputs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OperationResult execute(Map<String, String> outputs) {
		return OperationResult.SUCCESS;
	}
}
