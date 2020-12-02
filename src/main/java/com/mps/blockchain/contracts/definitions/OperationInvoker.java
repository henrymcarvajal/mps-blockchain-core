package com.mps.blockchain.contracts.definitions;

import java.util.Map;

import com.mps.blockchain.contracts.exceptions.MissingInputException;

public interface OperationInvoker {

	void buildInputs(String operationName, Map<String, String> inputs) throws MissingInputException;
	void execute(Map<String, Object> outputs);
	boolean supportsOperation(String operationName);
}
