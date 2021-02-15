package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.deploy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import com.mps.blockchain.commons.operations.GenericOperationOutputs;
import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.commons.operations.definitions.DeployOperationMetadata;
import com.mps.blockchain.commons.queue.messages.BlockchainOperationQueueRequest;
import com.mps.blockchain.commons.queue.messages.GenericMessageInputParameters;
import com.mps.blockchain.commons.queue.operations.messages.deploy.DeployContractMessageInputParameters;
import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.returnablesalev1.ReturnableSaleV1;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.BlockchainAccount;
import com.mps.blockchain.model.BuyerAccount;
import com.mps.blockchain.model.EnqueuedOperation;
import com.mps.blockchain.model.SellerAccount;
import com.mps.blockchain.network.NetworkProvider;
import com.mps.blockchain.persistence.services.BlockchainAccountRepositoryService;
import com.mps.blockchain.persistence.services.EnqueuedOperationRepositoryService;
import com.mps.blockchain.service.accounts.AccountFunder;
import com.mps.blockchain.service.accounts.AccountManager;
import com.mps.blockchain.service.accounts.CredentialsProvider;
import com.mps.blockchain.service.currencies.Currency;
import com.mps.blockchain.service.currencies.CurrencyConvertionService;
import com.mps.blockchain.service.currencies.MissingCurrencyConversionException;
import com.mps.blockchain.service.queue.BlockchainQueueService;
import com.mps.blockchain.utils.StringUtils;

@Component
public class Deploy implements ContractOperation {
    
    @Autowired
    private NetworkProvider networkProvider;
    
    @Autowired
    private CredentialsProvider credentialsProvider;
    
    @Autowired
    private AccountManager accountManager;
    
    @Autowired
    private AccountFunder accountFunder;
    
    @Autowired
    private CurrencyConvertionService currencyConvertionService;
    
    @Autowired
    private BlockchainAccountRepositoryService blockchainAccountRepositoryService;
    
    @Autowired
    private EnqueuedOperationRepositoryService enqueuedOperationRepositoryService;
    
    @Autowired
    private BlockchainQueueService queueService;
    
    private DeployInputParameters inputParameters;
    
    @Override
    public String getOperationName() {
        return DeployOperationMetadata.NAME;
    }
    
    @Override
    public void buildInputs(Map<String, String> inputs) throws MissingInputException {
        inputParameters = DeployInputParameters.build(inputs);
    }
    
    @Override
    public OperationResult execute(Map<String, String> outputs) {
        
        UUID transactionId = inputParameters.getTransactionId();
        if (transactionId == null) {
            throw new IllegalStateException("Previous call to buildInputs required: missing transactionId");
        }
        
        UUID sellerId = inputParameters.getSellerId();
        if (sellerId == null) {
            throw new IllegalStateException("Previous call to buildInputs required: missing sellerId");
        }
        
        SellerAccount sellerAccount;
        sellerAccount = accountManager.getSellerAccount(sellerId, true);
        
        UUID buyerId = inputParameters.getBuyerId();
        if (buyerId == null) {
            throw new IllegalStateException("Previous call to buildInputs required: missing buyerId");
        }
        
        BuyerAccount buyerAccount = null;
        buyerAccount = accountManager.getBuyerAccount(buyerId, true);
        
        BigDecimal contractValue = inputParameters.getContractValue();
        if (contractValue == null) {
            throw new IllegalStateException("Previous call to buildInputs required: missing contractValue");
        }
        
        Optional<BlockchainAccount> sellerAccountOptional = blockchainAccountRepositoryService
                .findById(sellerAccount.getBlockchainAccountId());
        if (sellerAccountOptional.isEmpty()) {
            outputs.put("error", "seller account not found: " + sellerAccount.getBlockchainAccountId());
            return OperationResult.ERROR;
        }
        BlockchainAccount sellerBlockchainAccount = sellerAccountOptional.get();
        
        Optional<BlockchainAccount> buyerAccountOptional = blockchainAccountRepositoryService
                .findById(buyerAccount.getBlockchainAccountId());
        if (buyerAccountOptional.isEmpty()) {
            outputs.put("error", "buyer account not found: " + sellerAccount.getBlockchainAccountId());
            return OperationResult.ERROR;
        }
        BlockchainAccount buyerBlockchainAccount = buyerAccountOptional.get();
        
        BigDecimal amount;
        try {
            amount = currencyConvertionService.convert(Currency.FIAT.COLOMBIAN_PESO, Currency.CRYPTO.XDAI,
                    contractValue);
            
            BigDecimal weiAmount = Convert.toWei(amount, Unit.ETHER);
            
            BlockchainOperationQueueRequest message = new BlockchainOperationQueueRequest();
            message.setName(DeployOperationMetadata.NAME);
            
            Map<String, String> payload = new HashMap<>();
            payload.put(GenericMessageInputParameters.NETWORK_ENDPOINT, networkProvider.getBlockchainConnectionURL());
            payload.put(DeployContractMessageInputParameters.MAIN_ACCOUNT_PRIVATE_KEY,
                    credentialsProvider.getMpsAccountPrivateK());
            payload.put(DeployContractMessageInputParameters.MAIN_ACCOUNT_PUBLIC_KEY,
                    credentialsProvider.getMpsAccountPublicK());
            payload.put(DeployContractMessageInputParameters.CONTRACT_AMOUNT,
                    weiAmount.stripTrailingZeros().toPlainString());
            payload.put(DeployContractMessageInputParameters.SELLER_ACCOUNT_ID, sellerAccount.getId().toString());
            payload.put(DeployContractMessageInputParameters.BUYER_ACCOUNT_ID, buyerAccount.getId().toString());
            payload.put(DeployContractMessageInputParameters.SELLER_ACCOUNT_ADDRESS,
                    sellerBlockchainAccount.getAddress());
            payload.put(DeployContractMessageInputParameters.BUYER_ACCOUNT_ADDRESS,
                    buyerBlockchainAccount.getAddress());
            payload.put(DeployContractMessageInputParameters.CONTRACT_DEFINITION_ID,
                    ReturnableSaleV1.CONTRACT_ID.toString());
            payload.put(DeployContractMessageInputParameters.TRANSACTION_ID, transactionId.toString());
            message.setPayload(payload);
            
            UUID id = UUID.randomUUID();
            message.setOperationId(id);
            
            EnqueuedOperation enqueuedOperation = new EnqueuedOperation();
            enqueuedOperation.setId(id);
            enqueuedOperation.setTransactionId(transactionId);
            enqueuedOperation.setRequest(message);
            enqueuedOperationRepositoryService.create(enqueuedOperation);
            
            queueService.sendRequestMessage(message);
            
            accountFunder.fundSellerAddress(sellerBlockchainAccount.getAddress(), transactionId);
            accountFunder.fundBuyerAddress(buyerBlockchainAccount.getAddress(), transactionId);
            
            return OperationResult.SUCCESS;
        } catch (MissingCurrencyConversionException e) {
            outputs.put(GenericOperationOutputs.ERROR_MESSAGE, StringUtils.toString(e));
            return OperationResult.ERROR;
        }
    }
}
