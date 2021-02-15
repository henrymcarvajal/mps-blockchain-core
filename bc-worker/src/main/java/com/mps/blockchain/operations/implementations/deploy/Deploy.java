package com.mps.blockchain.operations.implementations.deploy;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import com.mps.blockchain.commons.contracts.ReturnableSaleV1;
import com.mps.blockchain.commons.operations.GenericOperationOutputs;
import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.commons.operations.definitions.DeployOperationMetadata;
import com.mps.blockchain.commons.queue.messages.GenericMessageInputParameters;
import com.mps.blockchain.commons.queue.operations.messages.deploy.DeployContractMessageInputParameters;
import com.mps.blockchain.commons.queue.operations.messages.deploy.DeployContractMessageOutputParameters;
import com.mps.blockchain.operations.Operation;
import com.mps.blockchain.utils.StringUtils;

public class Deploy implements Operation {
    
    private Map<String, String> inputs;
    
    @Override
    public String getName() {
        return DeployOperationMetadata.NAME;
    }
    
    @Override
    public void setInputs(Map<String, String> inputs) {
        this.inputs = inputs;
    }
    
    @Override
    public Map<String, String> getInputs() {
        return this.inputs;
    }
    
    @Override
    public OperationResult execute(Map<String, String> outputs) {
        
        String networkEndpoint = inputs.get(GenericMessageInputParameters.NETWORK_ENDPOINT);
        String mpsAccountPrivateK = inputs.get(DeployContractMessageInputParameters.MAIN_ACCOUNT_PRIVATE_KEY);
        String mpsAccountPublicK = inputs.get(DeployContractMessageInputParameters.MAIN_ACCOUNT_PUBLIC_KEY);
        String contractAmount = inputs.get(DeployContractMessageInputParameters.CONTRACT_AMOUNT);
        String sellerAddress = inputs.get(DeployContractMessageInputParameters.SELLER_ACCOUNT_ADDRESS);
        String buyerAddress = inputs.get(DeployContractMessageInputParameters.BUYER_ACCOUNT_ADDRESS);
        
        try {
            Web3j web3j = Web3j.build(new HttpService(networkEndpoint));
            Credentials credentials = Credentials.create(mpsAccountPrivateK, mpsAccountPublicK);
            
            BigDecimal amount = new BigDecimal(contractAmount);
            
            TransactionManager txManager = new FastRawTransactionManager(web3j, credentials);
            
            ReturnableSaleV1 returnableSaleV1 = ReturnableSaleV1.deploy(web3j, txManager, new DefaultGasProvider(),
                    sellerAddress, buyerAddress, amount.toBigIntegerExact()).send();
            String contractAddress = returnableSaleV1.getContractAddress();
            Optional<TransactionReceipt> optionalReceipt = returnableSaleV1.getTransactionReceipt();
            
            if (optionalReceipt.isPresent()) {
                TransactionReceipt transactionReceipt = optionalReceipt.get();
                outputs.put(DeployContractMessageOutputParameters.TRANSACTION_RECEIPT,
                        StringUtils.toString(transactionReceipt));
            }
            
            outputs.put(DeployContractMessageOutputParameters.CONTRACT_ADDRESS, contractAddress);
            return OperationResult.SUCCESS;
        } catch (Exception e) {
            outputs.put(GenericOperationOutputs.ERROR_MESSAGE, StringUtils.toString(e));
            return OperationResult.ERROR;
        }
    }
}
