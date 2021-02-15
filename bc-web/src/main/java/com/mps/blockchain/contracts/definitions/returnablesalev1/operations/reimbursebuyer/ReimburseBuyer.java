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

import com.mps.blockchain.commons.contracts.ReturnableSaleV1;
import com.mps.blockchain.commons.operations.GenericOperationOutputs;
import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.BlockchainAccount;
import com.mps.blockchain.model.BuyerAccount;
import com.mps.blockchain.model.DeployedContract;
import com.mps.blockchain.network.NetworkProvider;
import com.mps.blockchain.persistence.services.BlockchainAccountRepositoryService;
import com.mps.blockchain.persistence.services.DeployedContractsRepositoryService;
import com.mps.blockchain.service.accounts.AccountManager;
import com.mps.blockchain.service.accounts.CredentialsProvider;
import com.mps.blockchain.utils.StringUtils;

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
    public OperationResult execute(Map<String, String> outputs) {
        
        UUID buyerId = inputParameters.getBuyerId();
        if (buyerId == null) {
            throw new IllegalStateException("Call to buildInputs required: missing buyerId");
        }
        
        BuyerAccount buyerAccount;
        buyerAccount = accountManager.getBuyerAccount(buyerId);
        if (buyerAccount == null) {
            outputs.put(GenericOperationOutputs.ERROR_MESSAGE, String.format("buyer not found: %s", buyerId));
            return OperationResult.ERROR;
        }
        
        Optional<BlockchainAccount> buyerAccountOptional = blockchainAccountRepositoryService
                .findById(buyerAccount.getBlockchainAccountId());
        if (buyerAccountOptional.isEmpty()) {
            outputs.put(GenericOperationOutputs.ERROR_MESSAGE,
                    "buyer account not found: " + buyerAccount.getBlockchainAccountId());
            return OperationResult.ERROR;
        }
        BlockchainAccount buyerBlockchainAccount = buyerAccountOptional.get();
        
        Optional<DeployedContract> deployedContractOptional = deployedContractsRepositoryService
                .findById(inputParameters.getContractId());
        
        if (deployedContractOptional.isEmpty()) {
            outputs.put(GenericOperationOutputs.ERROR_MESSAGE,
                    "contract not found: " + inputParameters.getContractId());
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
            outputs.put("receipt", transactionReceipt.toString());
            result = OperationResult.SUCCESS;
        } catch (Exception e) {
            outputs.put(GenericOperationOutputs.ERROR_MESSAGE, StringUtils.toString(e));
            result = OperationResult.ERROR;
        }
        return result;
    }
}
