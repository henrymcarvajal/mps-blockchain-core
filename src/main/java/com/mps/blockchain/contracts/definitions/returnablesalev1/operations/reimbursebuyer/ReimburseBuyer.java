package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.reimbursebuyer;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.OperationResult;
import com.mps.blockchain.contracts.definitions.returnablesalev1.ReturnableSaleV1;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.BuyerAccount;
import com.mps.blockchain.model.DecryptedBlockchainAccount;
import com.mps.blockchain.model.DeployedContract;
import com.mps.blockchain.network.NetworkProvider;
import com.mps.blockchain.persistence.services.BlockchainAccountRepositoryService;
import com.mps.blockchain.persistence.services.DeployedContractsRepositoryService;
import com.mps.blockchain.service.accounts.AccountManager;
import com.mps.blockchain.service.accounts.CredentialsProvider;

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
	public OperationResult execute(Map<String, Object> outputs) {

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
			return OperationResult.ERROR;
		}
		DecryptedBlockchainAccount buyerBlockchainAccount = buyerAccountOptional.get();

		Optional<DeployedContract> deployedContractOptional = deployedContractsRepositoryService
				.findById(inputParameters.getContractId());

		if (deployedContractOptional.isEmpty()) {
			outputs.put("error", "contract not found: " + inputParameters.getContractId());
			return OperationResult.ERROR;
		}
		DeployedContract deployedContract = deployedContractOptional.get();

		OperationResult result;
		try {
			Web3j web3j = networkProvider.getBlockchainNetwork();
			Credentials credentials = credentialsProvider.getCredentials(buyerBlockchainAccount);

			ReturnableSaleV1 returnableSaleV1 = ReturnableSaleV1.load(deployedContract.getAddress(), web3j, credentials,
					new DefaultGasProvider());

			RemoteCall<TransactionReceipt> transaction = returnableSaleV1.reimburseBuyer();
			TransactionReceipt transactionReceipt = transaction.send();
			
			String transactionHash = transactionReceipt.getTransactionHash();

			if (transactionHash != null) {

				Optional<TransactionReceipt> transactionReceiptO;
				do {
					System.out.println("checking if transaction " + transactionHash + " is mined....");
					EthGetTransactionReceipt ethGetTransactionReceiptResp = web3j
							.ethGetTransactionReceipt(transactionHash).send();
					transactionReceiptO = ethGetTransactionReceiptResp.getTransactionReceipt();
					Thread.sleep(3000); // Wait 3 sec
				} while (!transactionReceiptO.isPresent());

			}
			outputs.put("receipt", transactionReceipt);
			result = OperationResult.SUCCESS;
		} catch (Exception e) {
			outputs.put("error", e);
		    result = OperationResult.ERROR;
		}
		return result;
	}
}
