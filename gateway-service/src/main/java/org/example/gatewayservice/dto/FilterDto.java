package org.example.gatewayservice.dto;

import java.util.Map;

public record FilterDto(
        String name,
        Map<String, String> args
) {}