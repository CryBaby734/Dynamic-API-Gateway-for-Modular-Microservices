package org.example.dynamicroutingcore.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteRequest {


    private String routeId;
    private String uri;
    private List<PredicateDto> predicates;
    private List<FilterDto> filters;
    private Integer routeOrder;
    private Boolean enabled;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PredicateDto {
        private String name;
        private Map<String, String> args;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FilterDto {
        private String name;
        private Map<String, String> args;
    }

}
