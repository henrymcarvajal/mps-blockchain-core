package com.mps.blockchain.controller.callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mps.blockchain.commons.queue.messages.BlockchainOperationQueueResponse;
import com.mps.blockchain.service.queue.BlockchainQueueService;

@RestController
@RequestMapping(path = "/queue")
public class BlockchainQueueController {
    
    @Autowired
    private BlockchainQueueService blockchainQueueService;
    
    @PutMapping
    public ResponseEntity<String> updateBlockchainOperation(@RequestBody BlockchainOperationQueueResponse response) {
        String result = blockchainQueueService.receiveResponseMessage(response);
        return ResponseEntity.ok(result);
    }
}
