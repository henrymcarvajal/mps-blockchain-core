package com.mps.blockchain.service.contracts;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.contracts.ContractManager;
import com.mps.blockchain.contracts.definitions.ContractProvider;
import com.mps.blockchain.contracts.definitions.OperationInvoker;
import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.controller.request.TransactionResponse;
import com.mps.blockchain.model.Transaction;
import com.mps.blockchain.persistence.services.TransactionRepositoryService;

@Service
public class ContractsHandler {

	private static final String MPS_TRANSACTION_ID = "mpsTransactionId";
	private static final String CONTRACT_NAME = "contractName";
	private static final String OPERATION_NAME = "operationName";

	@Autowired
	private ContractManager contractManager;

	@Autowired
	private TransactionRepositoryService transactionRepositoryService;
	
	public TransactionResponse runTransaction(Map<String, String> inputs) throws MissingInputException {
		
		String mpsTransactionId = (String) inputs.get(MPS_TRANSACTION_ID);
		if (mpsTransactionId == null) {
			throw new MissingInputException(MPS_TRANSACTION_ID);
		}

		String contractName = (String) inputs.get(CONTRACT_NAME);
		if (contractName == null) {
			throw new MissingInputException(CONTRACT_NAME);
		}

		String operationName = (String) inputs.get(OPERATION_NAME);
		if (operationName == null) {
			throw new MissingInputException(OPERATION_NAME);
		}

		ContractProvider contractProvider = contractManager.getContractProvider(contractName);
		if (contractProvider == null) {
			TransactionResponse transactionResponse = new TransactionResponse();
			transactionResponse.setOperationResult("Contract not available: '" + contractName + "'");
			return transactionResponse;
		}

		OperationInvoker operationInvoker = contractProvider.getInvoker();
		if (!operationInvoker.supportsOperation(operationName)) {
			TransactionResponse transactionResponse = new TransactionResponse();
			transactionResponse
					.setOperationResult("Operation '" + operationName + "' not available for contract '" + contractName + "'");
			return transactionResponse;
		}

		Transaction transaction = new Transaction();
		transaction.setMpsTransactionId(UUID.fromString(mpsTransactionId));
		transaction.setInputs(inputs);
		
		transactionRepositoryService.create(transaction);
		inputs.put("transactionId", transaction.getId().toString());
		operationInvoker.buildInputs(operationName, inputs);

		Map<String, String> outputs = new HashMap<>();
		OperationResult result = operationInvoker.execute(outputs);
		
		transaction.setResult(result.getValue());
		transaction.setOutputs(outputs);
		transactionRepositoryService.update(transaction);

		TransactionResponse transactionResponse = new TransactionResponse();
		transactionResponse.setTransactionId(transaction.getId());
		transactionResponse.setOperationResult(result.getValue());
		if (outputs.containsKey("receipt")) {
			outputs.remove("receipt");
		}
		if (!outputs.isEmpty()) {
			transactionResponse.setData(outputs);
		}
		return transactionResponse;
	}
}
