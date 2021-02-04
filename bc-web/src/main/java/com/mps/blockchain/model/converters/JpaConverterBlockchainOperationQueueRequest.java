package com.mps.blockchain.model.converters;

import java.io.IOException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mps.blockchain.commons.queue.messages.BlockchainOperationQueueRequest;

@Converter(autoApply = true)
public class JpaConverterBlockchainOperationQueueRequest
        implements AttributeConverter<BlockchainOperationQueueRequest, String> {
    
    private final static ObjectMapper objectMapper = new ObjectMapper();
    
    public JpaConverterBlockchainOperationQueueRequest() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    @Override
    public String convertToDatabaseColumn(BlockchainOperationQueueRequest meta) {
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
            // or throw an error
        }
    }
    
    @Override
    public BlockchainOperationQueueRequest convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, BlockchainOperationQueueRequest.class);
        } catch (IOException ex) {
            // logger.error("Unexpected IOEx decoding json from database: " + dbData);
            ex.printStackTrace();
            return null;
        }
    }
    
}
