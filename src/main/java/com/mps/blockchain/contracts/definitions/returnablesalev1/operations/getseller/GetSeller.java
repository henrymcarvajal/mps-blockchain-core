package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.getseller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.DefaultGasProvider;

import com.mps.blockchain.CredentialsProvider;
import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.returnablesalev1.ReturnableSaleV1;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.DeployedContract;
import com.mps.blockchain.network.NetworkProvider;
import com.mps.blockchain.persistence.services.DeployedContractsRepositoryService;

@Component
public class GetSeller implements ContractOperation {

	@Autowired
	private NetworkProvider networkProvider;

	@Autowired
	private CredentialsProvider credentialsProvider;

	@Autowired
	private DeployedContractsRepositoryService deployedContractsRepositoryService;

	private GetSellerInputParameters inputParameters;

	@Override
	public String getOperationName() {
		return GetSeller.class.getSimpleName();
	}

	@Override
	public void buildInputs(Map<String, String> inputs) throws MissingInputException {
		inputParameters = GetSellerInputParameters.build(inputs);
	}

	@Override
	public void execute(Map<String, Object> outputs) {

		Optional<DeployedContract> deployedContractOptional = deployedContractsRepositoryService
				.findById(inputParameters.getContractId());

		if (deployedContractOptional.isEmpty()) {
			outputs.put("error", "contract not found: " + inputParameters.getContractId());
			return;
		}
		DeployedContract deployedContract = deployedContractOptional.get();

		try {
			Web3j web3 = networkProvider.getBlockchainNetwork();
			Credentials credentials = credentialsProvider.getMainCredentials();

			ReturnableSaleV1 returnableSaleV1 = ReturnableSaleV1.load(deployedContract.getAddress(), web3, credentials,
					new DefaultGasProvider());

			RemoteCall<String> transaction = returnableSaleV1.seller();
			String address = transaction.send();
			outputs.put("address", address);
		} catch (Exception e) {
			outputs.put("error", e);
		}
	}
}
