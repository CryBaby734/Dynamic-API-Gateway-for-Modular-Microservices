package org.example.gatewayservice.event;

import lombok.Data;
import org.example.gatewayservice.dto.RouteDefinitionDto;

import java.time.Instant;

@Data
public class RouteChangedEvent {

    private RouteEventType eventType;
    private String routeId;
    private Instant timestamp;
}
