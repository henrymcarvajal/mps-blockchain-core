package com.mps.blockchain.model.converters;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mps.blockchain.utils.StringUtils;

@Converter(autoApply = true)
public class JpaConverterMap implements AttributeConverter<Map<?, ?>, String> {
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(JpaConverterMap.class);
    
    public JpaConverterMap() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    @Override
    public String convertToDatabaseColumn(Map<?, ?> map) {
        try {
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Unable to serialize to a String: {}", StringUtils.toString(ex));
            return null;
            // or throw an error
        }
    }
    
    @Override
    public Map<?, ?> convertToEntityAttribute(String string) {
        try {
            return OBJECT_MAPPER.readValue(string, Map.class);
        } catch (IOException ex) {
            LOGGER.error("Unexpected IOEx decoding json from database: {}", string);
            return null;
            // or throw an error
        }
    }
}
