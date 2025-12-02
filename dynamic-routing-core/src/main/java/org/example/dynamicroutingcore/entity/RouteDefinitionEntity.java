package org.example.dynamicroutingcore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.Type;
import com.vladmihalcea.hibernate.type.json.JsonType;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "routes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteDefinitionEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "route_id", nullable = false, length = 100)
    private String routeId;

    @Column(nullable = false, length = 255)
    private String uri;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    private List<PredicateDefinition> predicates;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    private List<FilterDefinition> filters;

    @Column(name = "route_order")
    private Integer routeOrder;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}