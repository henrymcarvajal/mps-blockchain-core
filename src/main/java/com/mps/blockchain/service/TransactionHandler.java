package com.mps.blockchain.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mps.blockchain.contracts.ContractManager;
import com.mps.blockchain.contracts.definitions.ContractProvider;
import com.mps.blockchain.contracts.definitions.OperationInvoker;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.controller.request.TransactionResponse;
import com.mps.blockchain.model.Transaction;
import com.mps.blockchain.persistence.services.TransactionRepositoryService;

@Service
public class TransactionHandler {

	private static String TRANSACTION_ID = "transactionId";
	private static String CONTRACT_NAME_PARAM = "contractName";
	private static String OPERATION_NAME_PARAM = "operationName";

	@Autowired
	private ContractManager contractManager;

	@Autowired
	private TransactionRepositoryService transactionRepositoryService;
	
	public TransactionResponse runTransaction(Map<String, String> inputs) throws MissingInputException {
		
		String transactionId = (String) inputs.get(TRANSACTION_ID);
		if (transactionId == null) {
			throw new MissingInputException(TRANSACTION_ID);
		}

		String contractName = (String) inputs.get(CONTRACT_NAME_PARAM);
		if (contractName == null) {
			throw new MissingInputException(CONTRACT_NAME_PARAM);
		}

		String operationName = (String) inputs.get(OPERATION_NAME_PARAM);
		if (operationName == null) {
			throw new MissingInputException(OPERATION_NAME_PARAM);
		}

		ContractProvider contractProvider = contractManager.getContractProvider(contractName);
		if (contractProvider == null) {
			TransactionResponse transactionResponse = new TransactionResponse();
			transactionResponse.setResult("Contract not available: '" + contractName + "'");
			return transactionResponse;
		}

		OperationInvoker operationInvoker = contractProvider.getInvoker();
		if (!operationInvoker.supportsOperation(operationName)) {
			TransactionResponse transactionResponse = new TransactionResponse();
			transactionResponse
					.setResult("Operation '" + operationName + "' not available for contract '" + contractName + "'");
			return transactionResponse;
		}

		Transaction transaction = new Transaction();
		transaction.setIdTransaction(UUID.fromString(transactionId));
		transaction.setInputs(toString(inputs));
		
		transactionRepositoryService.create(transaction);
		
		operationInvoker.buildInputs(operationName, inputs);

		Map<String, Object> outputs = new HashMap<>();
		operationInvoker.execute(outputs);
		
		transaction.setOutputs(toString(outputs));
		transactionRepositoryService.update(transaction);

		TransactionResponse transactionResponse = new TransactionResponse();
		transactionResponse.setResult("Execution success");
		transactionResponse.setData(outputs);
		return transactionResponse;
	}
	
	private String toString(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
