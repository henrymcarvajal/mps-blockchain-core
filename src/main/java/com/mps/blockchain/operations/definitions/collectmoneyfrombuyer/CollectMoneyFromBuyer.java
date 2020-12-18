package com.mps.blockchain.operations.definitions.collectmoneyfrombuyer;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.contracts.definitions.OperationResult;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.BuyerAccount;
import com.mps.blockchain.model.DecryptedBlockchainAccount;
import com.mps.blockchain.operations.Operation;
import com.mps.blockchain.persistence.services.BlockchainAccountRepositoryService;
import com.mps.blockchain.service.accounts.AccountManager;
import com.mps.blockchain.service.accounts.AccountWithdrawer;
import com.mps.blockchain.service.accounts.CredentialsProvider;

@Service
public class CollectMoneyFromBuyer implements Operation {
	
	@Autowired
	private CredentialsProvider credentialsProvider;
	
	@Autowired
	private AccountManager accountManager;
	
	@Autowired
	private AccountWithdrawer accountWithdrawer;
	
	@Autowired
	private BlockchainAccountRepositoryService blockchainAccountRepositoryService;
	
	private CollectMoneyFromBuyerInputParameters inputParameters;

	@Override
	public String getOperationName() {
		return CollectMoneyFromBuyer.class.getSimpleName();
	}

	@Override
	public void buildInputs(Map<String, String> inputs) throws MissingInputException {
		inputParameters = CollectMoneyFromBuyerInputParameters.build(inputs);
	}

	@Override
	public OperationResult execute(Map<String, Object> outputs) {
		
		UUID buyerId = inputParameters.getBuyerId();
		if (buyerId == null) {
			throw new IllegalStateException("Call to buildInputs required: missing buyerId");
		}
		
		BuyerAccount buyerAccount;
		buyerAccount = accountManager.getBuyerAccount(buyerId);
		if (buyerAccount == null) {
			outputs.put("error",  String.format("buyer not found: %s", buyerId));
			return OperationResult.ERROR;
		}
		
		Optional<DecryptedBlockchainAccount> buyerAccountOptional = blockchainAccountRepositoryService
				.findById(buyerAccount.getBlockchainAccountId());
		if (buyerAccountOptional.isEmpty()) {
			outputs.put("error", "buyer account not found: " + buyerAccount.getBlockchainAccountId());
			return OperationResult.ERROR;
		}
		DecryptedBlockchainAccount buyerBlockchainAccount = buyerAccountOptional.get();

		return accountWithdrawer.withdrawFromBuyerAddress(credentialsProvider.getCredentials(buyerBlockchainAccount), outputs);
	}
}
