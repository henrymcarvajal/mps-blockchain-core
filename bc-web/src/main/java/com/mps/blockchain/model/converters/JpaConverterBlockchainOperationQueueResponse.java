package com.mps.blockchain.model.converters;

import java.io.IOException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mps.blockchain.commons.queue.messages.BlockchainOperationQueueResponse;

@Converter(autoApply = true)
public class JpaConverterBlockchainOperationQueueResponse implements AttributeConverter<BlockchainOperationQueueResponse, String> {
    
    private final static ObjectMapper objectMapper = new ObjectMapper();
    
    public JpaConverterBlockchainOperationQueueResponse() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    @Override
    public String convertToDatabaseColumn(BlockchainOperationQueueResponse meta) {
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            return null;
            // or throw an error
        }
    }
    
    @Override
    public BlockchainOperationQueueResponse convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, BlockchainOperationQueueResponse.class);
        } catch (IOException ex) {
            // logger.error("Unexpected IOEx decoding json from database: " + dbData);
            return null;
        }
    }
    
}
