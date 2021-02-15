package com.mps.blockchain.service.queue.processors;

import com.mps.blockchain.model.EnqueuedOperation;

public interface BlockchainMessageProcessor {
    boolean accept(EnqueuedOperation enqueuedOperation);
    
    void process();
}
