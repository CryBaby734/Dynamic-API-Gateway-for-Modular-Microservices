package org.example.dynamicroutingcore.controller;



import lombok.RequiredArgsConstructor;
import org.example.dynamicroutingcore.dto.RouteDefinitionDto;
import org.example.dynamicroutingcore.dto.RouteRequest;
import org.example.dynamicroutingcore.dto.RouteResponse;
import org.example.dynamicroutingcore.mapper.RouteMapper;
import org.example.dynamicroutingcore.service.RouteDefinitionService;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteDefinitionController {

    private final RouteDefinitionService service;

    @GetMapping
    public ResponseEntity<List<RouteResponse>> getAll() {
        return ResponseEntity.ok(
                service.getAllRoutes().stream()
                        .map(RouteMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/active")
    public ResponseEntity<List<RouteResponse>> getActiveRoutes() {
        return ResponseEntity.ok(
                service.getActiveRoutes().stream()
                        .map(RouteMapper::toResponse)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<RouteResponse> create(@RequestBody RouteRequest request) {
        var entity = service.create(request);
        return ResponseEntity.ok(RouteMapper.toResponse(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteResponse> update (@PathVariable UUID id,
                                                 @RequestBody RouteRequest request){
        var entity = service.update(id, request);
        return ResponseEntity.ok(RouteMapper.toResponse(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<Void> toggleEnabled (@PathVariable UUID id, @RequestParam boolean enabled) {
        service.toggleEnabled(id, enabled);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/definitions")
    public List<RouteDefinitionDto> getDefinitions() {
        return service.getActiveRoutes().stream()
                .map(RouteMapper::toDto)
                .toList();
    }


}
