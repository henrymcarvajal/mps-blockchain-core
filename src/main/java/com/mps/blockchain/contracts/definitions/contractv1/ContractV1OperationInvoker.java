package com.mps.blockchain.contracts.definitions.contractv1;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.OperationInvoker;
import com.mps.blockchain.contracts.definitions.OperationResult;
import com.mps.blockchain.contracts.definitions.compraventa.operations.sendsellerdeposit.CompraventaSendSellerDeposit;
import com.mps.blockchain.contracts.definitions.contractv1.operations.ContractV1DeployOperation;
import com.mps.blockchain.contracts.definitions.contractv1.operations.ContractV1ReleaseFundsOperation;
import com.mps.blockchain.contracts.definitions.contractv1.operations.ContractV1SendBuyerDepositOperation;

@Service
public class ContractV1OperationInvoker implements OperationInvoker {

	@Autowired
	private ContractV1DeployOperation deploy;

	@Autowired
	private ContractV1ReleaseFundsOperation releaseFunds;

	@Autowired
	private ContractV1SendBuyerDepositOperation sendBuyerDeposit;

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
	public void buildInputs(String operationName, Map<String, String> inputs) {
		ContractOperation operation = this.availableOperations.get(operationName);
		if (operation == null) {
			throw new IllegalArgumentException("Operation '" + operationName + "' not available");
		}
		currentOperation = operationName;
	}

	@Override
	public OperationResult execute(Map<String, Object> outputs) {
		ContractOperation operation = this.availableOperations.get(currentOperation);
		return operation.execute(outputs);
	}

	@Override
	public boolean supportsOperation(String operationName) {
		return this.availableOperations.containsKey(operationName);
	}
}
