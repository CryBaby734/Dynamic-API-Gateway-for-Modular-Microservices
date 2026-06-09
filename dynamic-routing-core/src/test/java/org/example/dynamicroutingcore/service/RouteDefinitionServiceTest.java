package org.example.dynamicroutingcore.service;

import org.example.dynamicroutingcore.dto.RouteRequest;
import org.example.dynamicroutingcore.entity.RouteDefinitionEntity;
import org.example.dynamicroutingcore.event.RouteChangedEvent;
import org.example.dynamicroutingcore.event.RouteEventType;
import org.example.dynamicroutingcore.observability.RouteLifecycleMetrics;
import org.example.dynamicroutingcore.repository.RouteDefinitionRepository;
import org.example.dynamicroutingcore.validator.RouteValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteDefinitionServiceTest {

    @Mock
    private RouteDefinitionRepository repository;

    @Mock
    private RouteValidator routeValidator;

    @Mock
    private RouteEventProducer routeEventProducer;

    @Mock
    private RouteLifecycleMetrics routeLifecycleMetrics;

    @InjectMocks
    private RouteDefinitionService service;

    private RouteRequest validRequest;
    private RouteDefinitionEntity existingRoute;
    private final UUID routeUuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        validRequest = RouteRequest.builder()
                .routeId("test-route")
                .uri("http://example.com")
                .predicates(
                        List.of(new RouteRequest.PredicateDto("Path", Collections.singletonMap("pattern", "/test/**"))))
                .filters(List.of(
                        new RouteRequest.FilterDto("AddRequestHeader", Collections.singletonMap("X-Test", "Value"))))
                .routeOrder(1)
                .enabled(true)
                .build();

        existingRoute = RouteDefinitionEntity.builder()
                .id(routeUuid)
                .routeId("test-route")
                .uri("http://example.com")
                .predicates(Collections.emptyList())
                .filters(Collections.emptyList())
                .enabled(true)
                .build();
    }

    @Test
    @DisplayName("Create should save route, publish event and increment metric")
    void create_shouldSaveAndPublish() {
        // Given
        when(repository.save(any(RouteDefinitionEntity.class))).thenAnswer(invocation -> {
            RouteDefinitionEntity entity = invocation.getArgument(0);
            return RouteDefinitionEntity.builder()
                    .id(routeUuid)
                    .routeId(entity.getRouteId())
                    .uri(entity.getUri())
                    .enabled(entity.getEnabled())
                    .build();
        });

        // When
        RouteDefinitionEntity result = service.create(validRequest);

        // Then
        verify(routeValidator).validate(validRequest);
        verify(repository).save(any(RouteDefinitionEntity.class));
        verify(routeLifecycleMetrics).increment(eq("created"), eq("test-route"));

        ArgumentCaptor<RouteChangedEvent> eventCaptor = ArgumentCaptor.forClass(RouteChangedEvent.class);
        verify(routeEventProducer).publish(eventCaptor.capture());

        RouteChangedEvent event = eventCaptor.getValue();
        assertThat(event.getEventType()).isEqualTo(RouteEventType.ROUTE_CREATED);
        assertThat(event.getRouteId()).isEqualTo("test-route");

        assertThat(result.getId()).isEqualTo(routeUuid);
    }

    @Test
    @DisplayName("Update should update entity, publish event and increment metric")
    void update_shouldUpdateAndPublish() {
        // Given
        when(repository.findById(routeUuid)).thenReturn(Optional.of(existingRoute));
        when(repository.save(any(RouteDefinitionEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RouteRequest updateRequest = RouteRequest.builder()
                .routeId("updated-route") // ID shouldn't theoretically change often but logic allows
                .uri("http://updated.com")
                .predicates(Collections.emptyList())
                .filters(Collections.emptyList())
                .routeOrder(2)
                .enabled(false)
                .build();

        // When
        RouteDefinitionEntity result = service.update(routeUuid, updateRequest);

        // Then
        verify(routeValidator).validate(updateRequest);
        verify(repository).save(existingRoute);
        verify(routeLifecycleMetrics).increment(eq("updated"), eq("updated-route")); // Metric uses new ID

        assertThat(result.getUri()).isEqualTo("http://updated.com");
        assertThat(result.getEnabled()).isFalse();

        ArgumentCaptor<RouteChangedEvent> eventCaptor = ArgumentCaptor.forClass(RouteChangedEvent.class);
        verify(routeEventProducer).publish(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getEventType()).isEqualTo(RouteEventType.ROUTE_UPDATED);
    }

    @Test
    @DisplayName("Update should throw exception if route not found")
    void update_shouldThrowIfNotFound() {
        // Given
        when(repository.findById(routeUuid)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> service.update(routeUuid, validRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Route does not exist");

        verify(repository, never()).save(any());
        verifyNoInteractions(routeEventProducer);
    }

    @Test
    @DisplayName("Delete should remove entity, publish event and increment metric")
    void delete_shouldRemoveAndPublish() {
        // Given
        when(repository.findById(routeUuid)).thenReturn(Optional.of(existingRoute));

        // When
        service.delete(routeUuid);

        // Then
        verify(repository).delete(existingRoute);
        verify(routeLifecycleMetrics).increment(eq("deleted"), eq("test-route"));

        ArgumentCaptor<RouteChangedEvent> eventCaptor = ArgumentCaptor.forClass(RouteChangedEvent.class);
        verify(routeEventProducer).publish(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getEventType()).isEqualTo(RouteEventType.ROUTE_DELETED);
    }

    @Test
    @DisplayName("Toggle enabled should update status and publish event")
    void toggleEnabled_shouldUpdateAndPublish() {
        // Given
        existingRoute.setEnabled(true);
        when(repository.findById(routeUuid)).thenReturn(Optional.of(existingRoute));

        // When
        service.toggleEnabled(routeUuid, false);

        // Then
        assertThat(existingRoute.getEnabled()).isFalse();
        verify(repository).save(existingRoute);
        verify(routeLifecycleMetrics).increment(eq("disabled"), eq("test-route"));

        ArgumentCaptor<RouteChangedEvent> eventCaptor = ArgumentCaptor.forClass(RouteChangedEvent.class);
        verify(routeEventProducer).publish(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getEventType()).isEqualTo(RouteEventType.ROUTE_DISABLED);
    }
}
