package org.example.gatewayservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class FilterDto {
    private String name;
    private Map<String, String> args;
}