package com.mps.blockchain.operations.definitions.collectmoneyfromseller;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.BlockchainAccount;
import com.mps.blockchain.model.SellerAccount;
import com.mps.blockchain.operations.Operation;
import com.mps.blockchain.persistence.services.BlockchainAccountRepositoryService;
import com.mps.blockchain.service.accounts.AccountManager;
import com.mps.blockchain.service.accounts.AccountWithdrawer;
import com.mps.blockchain.service.accounts.CredentialsProvider;

@Service
public class CollectMoneyFromSeller implements Operation {
    
    @Autowired
    private CredentialsProvider credentialsProvider;
    
    @Autowired
    private AccountManager accountManager;
    
    @Autowired
    private AccountWithdrawer accountWithdrawer;
    
    @Autowired
    private BlockchainAccountRepositoryService blockchainAccountRepositoryService;
    
    private CollectMoneyFromSellerInputParameters inputParameters;
    
    @Override
    public String getOperationName() {
        return CollectMoneyFromSeller.class.getSimpleName();
    }
    
    @Override
    public void buildInputs(Map<String, String> inputs) throws MissingInputException {
        inputParameters = CollectMoneyFromSellerInputParameters.build(inputs);
    }
    
    @Override
    public OperationResult execute(Map<String, String> outputs) {
        
        UUID sellerId = inputParameters.getSellerId();
        if (sellerId == null) {
            throw new IllegalStateException("Call to buildInputs required: missing sellerId");
        }
        
        SellerAccount sellerAccount;
        sellerAccount = accountManager.getSellerAccount(sellerId);
        if (sellerAccount == null) {
            outputs.put("error", String.format("seller not found: %s", sellerId));
            return OperationResult.ERROR;
        }
        
        Optional<BlockchainAccount> sellerAccountOptional = blockchainAccountRepositoryService
                .findById(sellerAccount.getBlockchainAccountId());
        if (sellerAccountOptional.isEmpty()) {
            outputs.put("error", "seller account not found: " + sellerAccount.getBlockchainAccountId());
            return OperationResult.ERROR;
        }
        BlockchainAccount sellerBlockchainAccount = sellerAccountOptional.get();
        
        return accountWithdrawer.withdrawFromSellerAddress(credentialsProvider.getCredentials(sellerBlockchainAccount),
                outputs);
    }
}
