package com.mps.blockchain.commons.queue.messages;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class BlockchainOperationQueueRequest implements Serializable {
    
    /**
     * IDE Generated Serial Version UID
     */
    private static final long serialVersionUID = -8820489277432437319L;
    
    private UUID operationId;
    private LocalDateTime date;
    private String name;
    private Map<String, String> payload;
    
    public BlockchainOperationQueueRequest() {
        this.date = LocalDateTime.now();
    }
    
    public UUID getOperationId() {
        return operationId;
    }
    
    public void setOperationId(UUID operationId) {
        this.operationId = operationId;
    }
    
    public LocalDateTime getDate() {
        return date;
    }
    
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Map<String, String> getPayload() {
        return payload;
    }
    
    public void setPayload(Map<String, String> params) {
        this.payload = params;
    }
    
    @Override
    public String toString() {
        return "BlockchainOperationMessage [id=" + operationId + ", date=" + date + ", name=" + name + ", params="
                + payload + "]";
    }
}
