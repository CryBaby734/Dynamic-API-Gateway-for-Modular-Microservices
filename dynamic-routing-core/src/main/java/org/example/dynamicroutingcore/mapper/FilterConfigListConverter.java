package org.example.dynamicroutingcore.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import jakarta.servlet.FilterConfig;

import java.util.List;

@Converter
public class FilterConfigListConverter implements AttributeConverter<List<FilterConfig>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<FilterConfig> attribute) {
        try {
            return attribute == null ? "[]" : objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert filters to JSON", e);
        }
    }

    @Override
    public List<FilterConfig> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isBlank()) {
                return List.of();
            }
            return objectMapper.readValue(
                    dbData,
                    new TypeReference<List<FilterConfig>>() {}
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to read filters JSON", e);
        }
    }
}