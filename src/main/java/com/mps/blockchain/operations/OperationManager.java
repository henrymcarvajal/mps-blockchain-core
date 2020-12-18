package com.mps.blockchain.operations;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationManager {

	private Map<String, Operation> availableOperations;

	@Autowired
	public OperationManager(List<Operation> providers) {
		availableOperations = providers.stream()
				.collect(Collectors.toMap(Operation::getOperationName, Function.identity()));
	}

	public Operation getOperation(String operationName) {
		if (availableOperations.containsKey(operationName)) {
			return availableOperations.get(operationName);
		}
		return null;
	}
}
