package com.mps.blockchain.contracts.definitions.contractv2.operations;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.OperationResult;

@Component
public class ContractV2OperationXOperation implements ContractOperation {

	@Override
	public String getOperationName() {
		return "OperationX";
	}

	@Override
	public void buildInputs(Map<String, String> inputs) {
	}

	@Override
	public OperationResult execute(Map<String, Object> outputs) {
		return OperationResult.SUCCESS;
	}
}
