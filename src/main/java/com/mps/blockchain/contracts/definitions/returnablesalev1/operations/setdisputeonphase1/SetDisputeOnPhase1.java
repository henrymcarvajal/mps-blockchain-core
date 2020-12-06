package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.setdisputeonphase1;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.OperationResult;
import com.mps.blockchain.contracts.definitions.returnablesalev1.ReturnableSaleV1;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.DeployedContract;
import com.mps.blockchain.network.NetworkProvider;
import com.mps.blockchain.persistence.services.DeployedContractsRepositoryService;
import com.mps.blockchain.service.accounts.CredentialsProvider;

@Component
public class SetDisputeOnPhase1 implements ContractOperation {

	@Autowired
	private NetworkProvider networkProvider;

	@Autowired
	private CredentialsProvider credentialsProvider;

	@Autowired
	private DeployedContractsRepositoryService deployedContractsRepositoryService;

	private SetDisputeOnPhase1InputParameters inputParameters;

	@Override
	public String getOperationName() {
		return SetDisputeOnPhase1.class.getSimpleName();
	}

	@Override
	public void buildInputs(Map<String, String> inputs) throws MissingInputException {
		inputParameters = SetDisputeOnPhase1InputParameters.build(inputs);
	}

	@Override
	public OperationResult execute(Map<String, Object> outputs) {

		Optional<DeployedContract> deployedContractOptional = deployedContractsRepositoryService
				.findById(inputParameters.getContractId());

		if (deployedContractOptional.isEmpty()) {
			outputs.put("error", "contract not found: " + inputParameters.getContractId());
			return OperationResult.ERROR;
		}
		DeployedContract deployedContract = deployedContractOptional.get();

		OperationResult result;
		try {
			Web3j web3 = networkProvider.getBlockchainNetwork();
			Credentials credentials = credentialsProvider.getMainCredentials();

			ReturnableSaleV1 returnableSaleV1 = ReturnableSaleV1.load(deployedContract.getAddress(), web3, credentials,
					new DefaultGasProvider());

			RemoteCall<TransactionReceipt> transaction = returnableSaleV1.setDisputeOnPhase1();
			TransactionReceipt transactionReceipt = transaction.send();
			outputs.put("receipt", transactionReceipt);
			result = OperationResult.SUCCESS;
		} catch (Exception e) {
			outputs.put("error", e);
		    result = OperationResult.ERROR;
		}
		return result;
	}
}
