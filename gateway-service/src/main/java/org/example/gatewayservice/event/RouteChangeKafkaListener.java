package org.example.gatewayservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RouteChangeKafkaListener {

    private final ApplicationEventPublisher publisher;

    @KafkaListener(
        topics = "route-changes",
        groupId = "gateway-service"
    )
    public void onEvent(RouteChangedEvent event) {
        log.info("📩 Route change event received: {}", event.getEventType());

        // 🔔 Просто сигналим gateway обновить маршруты
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }
}