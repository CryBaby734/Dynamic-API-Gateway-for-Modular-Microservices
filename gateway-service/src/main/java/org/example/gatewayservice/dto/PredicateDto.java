package org.example.gatewayservice.dto;

import java.util.Map;

public record PredicateDto(
        String name,
        Map<String, String> args
) {}