package com.mps.blockchain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mps.blockchain.commons.queue.messages.BlockchainOperationQueueRequest;
import com.mps.blockchain.commons.queue.messages.BlockchainOperationQueueResponse;
import com.mps.blockchain.model.converters.JpaConverterBlockchainOperationQueueRequest;
import com.mps.blockchain.model.converters.JpaConverterBlockchainOperationQueueResponse;

@Entity
@Table(name = "enqueued_operation")
public class EnqueuedOperation {
    
    @Id
    //@GeneratedValue
    private UUID id;
    @Column(name = "transaction_id")
    private UUID transactionId;
    @Convert(converter = JpaConverterBlockchainOperationQueueRequest.class)
    private BlockchainOperationQueueRequest request;
    @Convert(converter = JpaConverterBlockchainOperationQueueResponse.class)
    private BlockchainOperationQueueResponse response;
    @Column(name = "operation_result")
    private String result;
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
    
    public EnqueuedOperation() {
        this.createdDate = LocalDateTime.now();
    }
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }
    
    public BlockchainOperationQueueRequest getRequest() {
        return request;
    }
    
    public void setRequest(BlockchainOperationQueueRequest request) {
        this.request = request;
    }
    
    public BlockchainOperationQueueResponse getResponse() {
        return response;
    }
    
    public void setResponse(BlockchainOperationQueueResponse response) {
        this.response = response;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        if (this.createdDate == null) {
            this.createdDate = createdDate;
        }
    }
    
    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
    
    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
