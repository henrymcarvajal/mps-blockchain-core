package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.payseller;

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

import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.OperationResult;
import com.mps.blockchain.contracts.definitions.returnablesalev1.ReturnableSaleV1;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.DecryptedBlockchainAccount;
import com.mps.blockchain.model.DeployedContract;
import com.mps.blockchain.model.SellerAccount;
import com.mps.blockchain.network.NetworkProvider;
import com.mps.blockchain.persistence.services.BlockchainAccountRepositoryService;
import com.mps.blockchain.persistence.services.DeployedContractsRepositoryService;
import com.mps.blockchain.service.accounts.AccountManager;
import com.mps.blockchain.service.accounts.CredentialsProvider;

@Component
public class PaySeller implements ContractOperation {

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

	private PaySellerInputParameters inputParameters;

	@Override
	public String getOperationName() {
		return PaySeller.class.getSimpleName();
	}

	@Override
	public void buildInputs(Map<String, String> inputs) throws MissingInputException {
		inputParameters = PaySellerInputParameters.build(inputs);
	}

	@Override
	public OperationResult execute(Map<String, Object> outputs) {

		UUID sellerId = inputParameters.getSellerId();
		SellerAccount sellerAccount = null;
		if (sellerId != null) {
			outputs.put("error", "seller not found: " + inputParameters.getSellerId());
			sellerAccount = accountManager.getSellerAccount(sellerId);
		}

		Optional<DecryptedBlockchainAccount> sellerAccountOptional = blockchainAccountRepositoryService
				.findById(sellerAccount.getBlockchainAccountId());
		if (sellerAccountOptional.isEmpty()) {
			outputs.put("error", "seller account not found: " + sellerAccount.getBlockchainAccountId());
			return OperationResult.ERROR;
		}
		DecryptedBlockchainAccount sellerBlockchainAccount = sellerAccountOptional.get();

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
			Credentials credentials = credentialsProvider.getCredentials(sellerBlockchainAccount);

			ReturnableSaleV1 returnableSaleV1 = ReturnableSaleV1.load(deployedContract.getAddress(), web3, credentials,
					new DefaultGasProvider());

			RemoteCall<TransactionReceipt> transaction = returnableSaleV1.paySeller();
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
