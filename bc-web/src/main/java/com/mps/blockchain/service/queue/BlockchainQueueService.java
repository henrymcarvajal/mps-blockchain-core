package com.mps.blockchain.service.queue;

import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.commons.queue.messages.BlockchainOperationQueueRequest;
import com.mps.blockchain.commons.queue.messages.BlockchainOperationQueueResponse;
import com.mps.blockchain.model.EnqueuedOperation;
import com.mps.blockchain.persistence.services.EnqueuedOperationRepositoryService;
import com.mps.blockchain.service.queue.processors.BlockchainMessageProcessor;

@Service
public class BlockchainQueueService {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private EnqueuedOperationRepositoryService queuedOperationRepositoryService;
    
    private List<BlockchainMessageProcessor> availableProcessors;
    
    @Autowired
    public BlockchainQueueService(List<BlockchainMessageProcessor> discoveredProcessors) {
        availableProcessors = discoveredProcessors;
    }
    
    public void sendRequestMessage(BlockchainOperationQueueRequest blockchainOperationMessage) {
        rabbitTemplate.convertAndSend(blockchainOperationMessage);
    }
    
    public String receiveResponseMessage(BlockchainOperationQueueResponse response) {
        Optional<EnqueuedOperation> optionalOfQueuedOperation = queuedOperationRepositoryService
                .findById(response.getOperationId());
        if (optionalOfQueuedOperation.isPresent()) {
            EnqueuedOperation queuedOperation = optionalOfQueuedOperation.get();
            queuedOperation.setResult(response.getResult().getValue());
            queuedOperation.setResponse(response);
            queuedOperationRepositoryService.update(queuedOperation);
            
            availableProcessors.forEach(processor -> {
                if (processor.accept(queuedOperation)) {
                    processor.process();
                }
            });
            
            return response.getOperationId().toString();
        }
        return null;
    }
}
