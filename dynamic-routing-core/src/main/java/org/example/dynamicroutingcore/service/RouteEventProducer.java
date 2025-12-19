package org.example.dynamicroutingcore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dynamicroutingcore.event.RouteChangedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class RouteEventProducer {

    private static final String TOPIC = "route-changes";

    private final KafkaTemplate<String, RouteChangedEvent> kafkaTemplate;

    public void publish(RouteChangedEvent event) {
        event.setTimestamp(Instant.now());

        kafkaTemplate.send(TOPIC, event.getRouteId().toString(), event);

        log.info("📤 Route event published: {} for route {}",
                event.getEventType(), event.getRouteId());
    }
}