package org.example.gatewayservice.dto;

import lombok.Data;


import java.util.List;

@Data
public class RouteDefinitionResponse {
    private String id;
    private String uri;
    private boolean enabled;
    private int order;

    private List<PredicateDto> predicates;
    private List<FilterDto> filters;

}