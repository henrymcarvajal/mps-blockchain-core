package com.mps.blockchain.contracts.definitions;

import java.util.Map;

import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

public interface OperationInvoker {

	void buildInputs(String operationName, Map<String, String> inputs) throws MissingInputException;
	OperationResult execute(Map<String, String> outputs);
	boolean supportsOperation(String operationName);
}
