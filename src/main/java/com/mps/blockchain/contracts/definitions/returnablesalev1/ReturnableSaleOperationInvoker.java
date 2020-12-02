package com.mps.blockchain.contracts.definitions.returnablesalev1;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.OperationInvoker;
import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.deploy.Deploy;
import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.fundbuyerfee.FundBuyerFee;
import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.fundsellerfee.FundSellerFee;
import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.getbuyer.GetBuyer;
import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.getseller.GetSeller;
import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.payseller.PaySeller;
import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.recoverfunds.RecoverFunds;
import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.reimbursebuyer.ReimburseBuyer;
import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.reimburseseller.ReimburseSeller;
import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.setdisputeonphase1.SetDisputeOnPhase1;
import com.mps.blockchain.contracts.definitions.returnablesalev1.operations.setdisputewinner.SetDisputeWinner;
import com.mps.blockchain.contracts.exceptions.MissingInputException;

@Service
public class ReturnableSaleOperationInvoker implements OperationInvoker {

	@Autowired
	private Deploy deploy;

	@Autowired
	private FundBuyerFee fundBuyerFee;

	@Autowired
	private FundSellerFee fundSellerFee;

	@Autowired
	private PaySeller paySeller;

	@Autowired
	private RecoverFunds recoverFunds;

	@Autowired
	private ReimburseBuyer reimburseBuyer;

	@Autowired
	private ReimburseSeller reimburseSeller;

	@Autowired
	private SetDisputeOnPhase1 setDisputeOnPhase1;

	@Autowired
	private SetDisputeWinner setDisputeWinner;

	@Autowired
	private GetSeller getSeller;

	@Autowired
	private GetBuyer getBuyer;

	private Map<String, ContractOperation> availableOperations = new HashMap<>();
	private String currentOperation = null;

	@PostConstruct
	private void registerOperations() {
		this.availableOperations.put(deploy.getOperationName(), deploy);
		this.availableOperations.put(fundBuyerFee.getOperationName(), fundBuyerFee);
		this.availableOperations.put(fundSellerFee.getOperationName(), fundSellerFee);
		this.availableOperations.put(paySeller.getOperationName(), paySeller);
		this.availableOperations.put(recoverFunds.getOperationName(), recoverFunds);
		this.availableOperations.put(reimburseBuyer.getOperationName(), reimburseBuyer);
		this.availableOperations.put(reimburseSeller.getOperationName(), reimburseSeller);
		this.availableOperations.put(setDisputeOnPhase1.getOperationName(), setDisputeOnPhase1);
		this.availableOperations.put(setDisputeWinner.getOperationName(), setDisputeWinner);

		this.availableOperations.put(getSeller.getOperationName(), getSeller);
		this.availableOperations.put(getBuyer.getOperationName(), getBuyer);
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
	public void execute(Map<String, Object> outputs) {
		ContractOperation operation = this.availableOperations.get(currentOperation);
		operation.execute(outputs);
	}

	@Override
	public boolean supportsOperation(String operationName) {
		return this.availableOperations.containsKey(operationName);
	}

}
