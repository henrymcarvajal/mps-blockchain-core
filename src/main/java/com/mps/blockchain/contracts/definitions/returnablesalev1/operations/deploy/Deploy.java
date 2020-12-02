package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.deploy;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mps.blockchain.CredentialsProvider;
import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.returnablesalev1.ReturnableSaleV1;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.BuyerAccount;
import com.mps.blockchain.model.DecryptedBlockchainAccount;
import com.mps.blockchain.model.DeployedContract;
import com.mps.blockchain.model.SellerAccount;
import com.mps.blockchain.network.NetworkProvider;
import com.mps.blockchain.persistence.services.BlockchainAccountRepositoryService;
import com.mps.blockchain.persistence.services.DeployedContractsRepositoryService;
import com.mps.blockchain.service.AccountManager;

@Component
public class Deploy implements ContractOperation {

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

	@Override
	public String getOperationName() {
		return Deploy.class.getSimpleName();
	}

	private DeployInputParameters inputParameters;

	@Override
	public void buildInputs(Map<String, String> inputs) throws MissingInputException {
		inputParameters = DeployInputParameters.build(inputs);
	}

	@Override
	public void execute(Map<String, Object> outputs) {
		UUID buyerId = inputParameters.getBuyerId();
		BuyerAccount buyerAccount = null;
		if (buyerId != null) {
			buyerAccount = accountManager.getBuyerAccount(buyerId);
		}

		UUID sellerId = inputParameters.getSellerId();
		SellerAccount sellerAccount = null;
		if (sellerId != null) {
			sellerAccount = accountManager.getSellerAccount(sellerId);
		}

		Optional<DecryptedBlockchainAccount> sellerAccountOptional = blockchainAccountRepositoryService
				.findById(sellerAccount.getBlockchainAccountId());
		if (sellerAccountOptional.isEmpty()) {
			return;
		}
		DecryptedBlockchainAccount sellerBlockchainAccount = sellerAccountOptional.get();

		Optional<DecryptedBlockchainAccount> buyerAccountOptional = blockchainAccountRepositoryService
				.findById(buyerAccount.getBlockchainAccountId());
		if (buyerAccountOptional.isEmpty()) {
			return;
		}
		DecryptedBlockchainAccount buyerBlockchainAccount = buyerAccountOptional.get();

		String contractAddress = "";
		String receipt = null;
		String deploymentResult = "success";

		Web3j web3j = networkProvider.getBlockchainNetwork();

		try {			
			Credentials credentials = credentialsProvider.getMainCredentials();

			ReturnableSaleV1 returnableSaleV1 = ReturnableSaleV1
					.deploy(web3j, credentials, new DefaultGasProvider(), sellerBlockchainAccount.getAddress(),
							buyerBlockchainAccount.getAddress(), inputParameters.getContractValue())
					.send();
			contractAddress = returnableSaleV1.getContractAddress();
			Optional<TransactionReceipt> optionalReceipt = returnableSaleV1.getTransactionReceipt();
			if (optionalReceipt.isPresent()) {
				TransactionReceipt transactionReceipt = optionalReceipt.get();
				receipt = toString(transactionReceipt);
			}
			DeployedContract contract = new DeployedContract();
			contract.setIdContract(ReturnableSaleV1.CONTRACT_ID);
			contract.setAddress(contractAddress);
			contract.setSellerAccountId(sellerAccount.getId());
			contract.setBuyerAccountId(buyerAccount.getId());
			contract.setReceipt(receipt);
			contract.setDeploymentResult(deploymentResult);
			deployedContractsRepositoryService.create(contract);

			outputs.put("contractId", contract.getId());
		} catch (Exception e) {
			deploymentResult = "error";
			receipt = toString(e);
		}
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
