package com.mps.blockchain.service.accounts;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.commons.operations.definitions.TransferOperationMetadata;
import com.mps.blockchain.commons.queue.messages.BlockchainOperationQueueRequest;
import com.mps.blockchain.commons.queue.operations.messages.transfer.TransferMessageInputParameters;
import com.mps.blockchain.model.EnqueuedOperation;
import com.mps.blockchain.network.NetworkProvider;
import com.mps.blockchain.persistence.services.EnqueuedOperationRepositoryService;
import com.mps.blockchain.service.queue.BlockchainQueueService;

@Component
public class AccountFunder {
    
    @Value("${blockchain.funds.seller.amount}")
    private Long sellerFundsAmount;
    
    @Value("${blockchain.funds.buyer.amount}")
    private Long buyerFundsAmount;
    
    @Autowired
    private NetworkProvider networkProvider;
    
    @Autowired
    private CredentialsProvider credentialsProvider;
    
    @Autowired
    private BlockchainQueueService queueService;
    
    @Autowired
    private EnqueuedOperationRepositoryService queuedOperationRepositoryService;
    
    public OperationResult fundSellerAddress(String address, UUID transactionId) {
        return fundAddress(address, sellerFundsAmount, transactionId);
    }
    
    public OperationResult fundBuyerAddress(String address, UUID transactionId) {
        return fundAddress(address, buyerFundsAmount, transactionId);
    }
    
    private OperationResult fundAddress(String address, long amount, UUID transactionId) {
        
        BlockchainOperationQueueRequest message = new BlockchainOperationQueueRequest();
        message.setName(TransferOperationMetadata.NAME);
        
        Map<String, String> payload = new HashMap<>();
        payload.put(TransferMessageInputParameters.NETWORK_ENDPOINT, networkProvider.getBlockchainConnectionURL());
        payload.put(TransferMessageInputParameters.MAIN_ACCOUNT_ADDRESS, credentialsProvider.getMainAddress());
        payload.put(TransferMessageInputParameters.MAIN_ACCOUNT_PRIVATE_K, credentialsProvider.getMpsAccountPrivateK());
        payload.put(TransferMessageInputParameters.MAIN_ACCOUNT_PUBLIC_K, credentialsProvider.getMpsAccountPublicK());
        payload.put(TransferMessageInputParameters.TRANSFER_ADDRESS, address);
        payload.put(TransferMessageInputParameters.TRANSFER_AMOUNT, Long.toString(amount));
        message.setPayload(payload);
        
        UUID id = UUID.randomUUID();
        message.setOperationId(id);
        
        EnqueuedOperation queuedOperation = new EnqueuedOperation();
        queuedOperation.setId(id);
        queuedOperation.setTransactionId(transactionId);
        queuedOperation.setRequest(message);
        queuedOperationRepositoryService.create(queuedOperation);
        
        message.setOperationId(queuedOperation.getId());
        queueService.sendRequestMessage(message);
        
        return OperationResult.SUCCESS;
        
    }
}
