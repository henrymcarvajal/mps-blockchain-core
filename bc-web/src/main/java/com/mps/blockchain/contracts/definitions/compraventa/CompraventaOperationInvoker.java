package com.mps.blockchain.contracts.definitions.compraventa;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.OperationInvoker;
import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.contracts.definitions.compraventa.operations.deploy.CompraventaDeploy;
import com.mps.blockchain.contracts.definitions.compraventa.operations.releasefunds.CompraventaReleaseFunds;
import com.mps.blockchain.contracts.definitions.compraventa.operations.sendbuyerdeposit.CompraventaSendBuyerDeposit;
import com.mps.blockchain.contracts.definitions.compraventa.operations.sendsellerdeposit.CompraventaSendSellerDeposit;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

@Service
public class CompraventaOperationInvoker implements OperationInvoker {

	@Autowired
	private CompraventaDeploy deploy;

	@Autowired
	private CompraventaReleaseFunds releaseFunds;

	@Autowired
	private CompraventaSendBuyerDeposit sendBuyerDeposit;

	@Autowired
	private CompraventaSendSellerDeposit sendSellerDeposit;

	private Map<String, ContractOperation> availableOperations = new HashMap<>();
	private String currentOperation = null;

	@PostConstruct
	private void registerOperations() {
		this.availableOperations.put(deploy.getOperationName(), deploy);
		this.availableOperations.put(releaseFunds.getOperationName(), releaseFunds);
		this.availableOperations.put(sendBuyerDeposit.getOperationName(), sendBuyerDeposit);
		this.availableOperations.put(sendSellerDeposit.getOperationName(), sendSellerDeposit);
	}

	@Override
	public void buildInputs(String operationName, Map<String, String> inputs) throws MissingInputException {
		ContractOperation operation = this.availableOperations.get(operationName);
		if (operation == null) {
			throw new IllegalArgumentException("Operation '" + operationName + "' not available");
		}
		currentOperation = operationName;
		operation.buildInputs(inputs);
	}

	@Override
	public OperationResult execute(Map<String, String> outputs) {
		ContractOperation operation = this.availableOperations.get(currentOperation);
		return operation.execute(outputs);
	}

	@Override
	public boolean supportsOperation(String operationName) {
		return this.availableOperations.containsKey(operationName);
	}

}
