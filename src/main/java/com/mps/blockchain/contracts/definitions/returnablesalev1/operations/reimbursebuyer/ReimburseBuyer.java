package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.reimbursebuyer;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import com.mps.blockchain.CredentialsProvider;
import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.returnablesalev1.ReturnableSaleV1;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.BuyerAccount;
import com.mps.blockchain.model.DecryptedBlockchainAccount;
import com.mps.blockchain.model.DeployedContract;
import com.mps.blockchain.network.NetworkProvider;
import com.mps.blockchain.persistence.services.BlockchainAccountRepositoryService;
import com.mps.blockchain.persistence.services.DeployedContractsRepositoryService;
import com.mps.blockchain.service.AccountManager;

@Component
public class ReimburseBuyer implements ContractOperation {

	@Autowired
	private NetworkProvider networkProvider;

	@Autowired
	private CredentialsProvider credentialsProvider;

	@Autowired
	private AccountManager accountManager;

	@Autowired
	private BlockchainAccountRepositoryService blockchainAccountRepositoryService;

	@Autowired
	private DeployedContractsRepositoryService deployedContractsRepositoryService;

	private ReimburseBuyerInputParameters inputParameters;

	@Override
	public String getOperationName() {
		return ReimburseBuyer.class.getSimpleName();
	}

	@Override
	public void buildInputs(Map<String, String> inputs) throws MissingInputException {
		inputParameters = ReimburseBuyerInputParameters.build(inputs);
	}

	@Override
	public void execute(Map<String, Object> outputs) {

		UUID buyerId = inputParameters.getBuyerId();
		BuyerAccount buyerAccount = null;
		if (buyerId != null) {
			outputs.put("error", "buyer not found: " + inputParameters.getBuyerId());
			buyerAccount = accountManager.getBuyerAccount(buyerId);
		}

		Optional<DecryptedBlockchainAccount> buyerAccountOptional = blockchainAccountRepositoryService
				.findById(buyerAccount.getBlockchainAccountId());
		if (buyerAccountOptional.isEmpty()) {
			outputs.put("error", "buyer account not found: " + buyerAccount.getBlockchainAccountId());
			return;
		}
		DecryptedBlockchainAccount buyerBlockchainAccount = buyerAccountOptional.get();

		Optional<DeployedContract> deployedContractOptional = deployedContractsRepositoryService
				.findById(inputParameters.getContractId());

		if (deployedContractOptional.isEmpty()) {
			outputs.put("error", "contract not found: " + inputParameters.getContractId());
			return;
		}
		DeployedContract deployedContract = deployedContractOptional.get();

		try {
			Web3j web3 = networkProvider.getBlockchainNetwork();
			Credentials credentials = credentialsProvider.getCredentials(buyerBlockchainAccount);

			ReturnableSaleV1 returnableSaleV1 = ReturnableSaleV1.load(deployedContract.getAddress(), web3, credentials,
					new DefaultGasProvider());

			RemoteCall<TransactionReceipt> transaction = returnableSaleV1.reimburseBuyer();
			TransactionReceipt transactionReceipt = transaction.send();
			outputs.put("receipt", transactionReceipt);
		} catch (Exception e) {
			outputs.put("error", e);
		}
	}
}
