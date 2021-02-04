package com.mps.blockchain.model.converters;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Converter(autoApply = true)
public class JpaConverterMap implements AttributeConverter<Map<?, ?>, String> {
    
    private final static ObjectMapper objectMapper = new ObjectMapper();
    
    public JpaConverterMap() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    @Override
    public String convertToDatabaseColumn(Map<?, ?> meta) {
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
            // or throw an error
        }
    }
    
    @Override
    public Map<?, ?> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, Map.class);
        } catch (IOException ex) {
            // logger.error("Unexpected IOEx decoding json from database: " + dbData);
            ex.printStackTrace();
            return null;
        }
    }
    
}
