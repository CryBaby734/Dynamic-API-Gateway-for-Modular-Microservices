package org.example.dynamicroutingcore.repository;

import org.example.dynamicroutingcore.entity.RouteDefinitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RouteDefinitionRepository extends JpaRepository<RouteDefinitionEntity, UUID> {

    List<RouteDefinitionEntity> findAllByEnabledTrueOrderByRouteOrderAsc();

    int countByEnabledTrue();

    int countByEnabledFalse();
}
