package com.mps.blockchain.service.queue.processors.impl;

import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClientException;

import com.mps.blockchain.commons.operations.definitions.DeployOperationMetadata;
import com.mps.blockchain.commons.queue.operations.messages.deploy.DeployContractMessageInputParameters;
import com.mps.blockchain.commons.queue.operations.messages.deploy.DeployContractMessageOutputParameters;
import com.mps.blockchain.model.DeployedContract;
import com.mps.blockchain.model.EnqueuedOperation;
import com.mps.blockchain.model.Transaction;
import com.mps.blockchain.persistence.services.DeployedContractsRepositoryService;
import com.mps.blockchain.persistence.services.TransactionRepositoryService;
import com.mps.blockchain.service.queue.processors.BlockchainMessageProcessor;

@Service
public class DeployedContractResponseMessageProcessor implements BlockchainMessageProcessor {
    
    @Value(value = "${mipagoseguro.payment.url}")
    private URL paymentURL;
    
    @Autowired
    private TransactionRepositoryService transactionRepositoryService;
    
    @Autowired
    private DeployedContractsRepositoryService deployedContractsRepositoryService;
    
    private EnqueuedOperation enqueuedOperation;
    
    @Override
    public boolean accept(EnqueuedOperation enqueuedOperation) {
        if (enqueuedOperation.getRequest().getName().contentEquals(DeployOperationMetadata.NAME)) {
            this.enqueuedOperation = enqueuedOperation;
            return true;
        }
        return false;
    }
    
    @Override
    public void process() {
        UUID uuid = enqueuedOperation.getTransactionId();
        Optional<Transaction> transactionOptional = transactionRepositoryService.findById(uuid);
        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();
            UUID contractId = saveContract();
            updatePayment(transaction.getMpsTransactionId(), contractId);
        }
    }
    
    private UUID saveContract() {
        DeployedContract contract = new DeployedContract();
        contract.setOperationId(enqueuedOperation.getId());
        
        Map<String, String> responseOutputs = enqueuedOperation.getResponse().getOutputs();
        
        contract.setAddress(responseOutputs.get(DeployContractMessageOutputParameters.CONTRACT_ADDRESS));
        contract.setReceipt(responseOutputs.get(DeployContractMessageOutputParameters.TRANSACTION_RECEIPT));
        
        Map<String, String> requestPayload = enqueuedOperation.getRequest().getPayload();
        
        UUID contractDefinitionId = UUID
                .fromString(requestPayload.get(DeployContractMessageInputParameters.CONTRACT_DEFINITION_ID));
        contract.setIdContract(contractDefinitionId);
        
        UUID sellerId = UUID.fromString(requestPayload.get(DeployContractMessageInputParameters.SELLER_ACCOUNT_ID));
        contract.setSellerAccountId(sellerId);
        
        UUID buyerId = UUID.fromString(requestPayload.get(DeployContractMessageInputParameters.BUYER_ACCOUNT_ID));
        contract.setBuyerAccountId(buyerId);
        
        deployedContractsRepositoryService.create(contract);
        return contract.getId();
    }
    
    private void updatePayment(UUID mpsTransactionId, UUID contractId) {
        String fullPaymentURL = String.format("%s/%s", paymentURL, mpsTransactionId.toString());
        RequestHeadersSpec<?> request = WebClient.create(fullPaymentURL).put()
                .body(BodyInserters.fromValue(contractId));
        try {
            request.retrieve().bodyToMono(String.class).block();
        } catch (WebClientException ex) {
            ex.printStackTrace();
        }
    }
}
