package org.example.dynamicroutingcore.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dynamicroutingcore.dto.RouteDefinitionDto;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteChangedEvent {

    private RouteEventType eventType;
    private String routeId;
    private Instant timestamp;
}
