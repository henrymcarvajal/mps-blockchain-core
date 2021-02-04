package com.mps.blockchain.commons.queue.messages;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.mps.blockchain.commons.operations.OperationResult;

public class BlockchainOperationQueueResponse implements Serializable {
    
    /**
     * IDE Generated Serial Version UID
     */
    private static final long serialVersionUID = -8151719976548581469L;
    
    private UUID operationId;
    private LocalDateTime date;
    private OperationResult result;
    private Map<String, String> outputs;
    
    public BlockchainOperationQueueResponse() {
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
    
    public OperationResult getResult() {
        return result;
    }
    
    public void setResult(OperationResult result) {
        this.result = result;
    }
    
    public Map<String, String> getOutputs() {
        return outputs;
    }
    
    public void setOutputs(Map<String, String> params) {
        this.outputs = params;
    }
    
    @Override
    public String toString() {
        return "BlockchainOperationQueueResponse [id=" + operationId + ", date=" + date + ", result=" + result
                + ", outputs=" + outputs + "]";
    }
}
