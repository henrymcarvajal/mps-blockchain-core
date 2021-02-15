package com.mps.blockchain.model.converters;

import java.io.IOException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mps.blockchain.commons.queue.messages.BlockchainOperationQueueResponse;
import com.mps.blockchain.utils.StringUtils;

@Converter(autoApply = true)
public class JpaConverterBlockchainOperationQueueResponse
        implements AttributeConverter<BlockchainOperationQueueResponse, String> {
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(JpaConverterBlockchainOperationQueueResponse.class);
    
    public JpaConverterBlockchainOperationQueueResponse() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    @Override
    public String convertToDatabaseColumn(BlockchainOperationQueueResponse object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Unable to serialize to a String: {}", StringUtils.toString(ex));
            return null;
            // or throw an error
        }
    }
    
    @Override
    public BlockchainOperationQueueResponse convertToEntityAttribute(String string) {
        try {
            return OBJECT_MAPPER.readValue(string, BlockchainOperationQueueResponse.class);
        } catch (IOException ex) {
            LOGGER.error("Unexpected IOEx decoding json from database: {}", string);
            return null;
            // or throw an error
        }
    }
}
