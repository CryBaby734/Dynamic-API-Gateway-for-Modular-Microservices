Dynamic API Gateway for Modular Microservices

This project implements a production-oriented dynamic API Gateway built with Spring Cloud Gateway.
It provides runtime-configurable routing, centralized administration, event-driven synchronization, and full observability.

The system is designed to reflect real-world gateway requirements: dynamic behavior, reliability, security, and operational visibility.

System Overview

The gateway acts as a single entry point for multiple backend services.
Routes are managed externally and propagated to gateway instances without restarts.

Key characteristics:

Dynamic route lifecycle management

Event-driven synchronization across instances

Centralized admin control plane

Metrics, tracing, and operational monitoring

Architecture
Components

Gateway Service

Spring Cloud Gateway (WebFlux)

Dynamic route loading at runtime

Global request/response filters

JWT authentication and authorization

Circuit breakers (Resilience4j)

Redis integration

Prometheus metrics export

Dynamic Routing Core

REST API for route administration

PostgreSQL persistence

Kafka event producer

Route validation and ordering logic

Route lifecycle metrics

Infrastructure Services

Eureka (service discovery)

Kafka (event streaming)

Prometheus (metrics collection)

Grafana (visualization)

Zipkin (distributed tracing)

Dynamic Routing Model

Routes are managed centrally and applied dynamically.

Supported Operations

Create route

Update route

Enable route

Disable route

Delete route

Event Types

ROUTE_CREATED

ROUTE_UPDATED

ROUTE_ENABLED

ROUTE_DISABLED

ROUTE_DELETED

Each operation:

Persists route state in the database

Emits a Kafka event

Triggers gateway route refresh

Produces lifecycle metrics

No gateway restart is required.

Security

JWT-based authentication

Role-based authorization (ADMIN / USER)

Admin endpoints protected

Authentication and authorization outcomes observable via metrics

Observability
Metrics (Prometheus)

Gateway Metrics

gateway_requests_total

gateway_error_total

gateway_request_duration_seconds

Histogram with percentiles (P50, P85, P95)

Routing Core Metrics

route_lifecycle_events_total

Labels: created, updated, enabled, disabled, deleted

Metrics are exposed via /actuator/prometheus.

Dashboards (Grafana)

Dashboards provide visibility into:

Request volume per route

Error rate per route

Gateway latency (P95)

Route lifecycle activity

System health indicators

Distributed Tracing

Zipkin integration

End-to-end request tracing

Gateway-to-service latency analysis

Configuration
Actuator Exposure
management:
endpoints:
web:
exposure:
include: health,info,prometheus

Prometheus Scrape Configuration
scrape_configs:
- job_name: 'gateway-service'
  metrics_path: '/actuator/prometheus'
  static_configs:
    - targets: ['host.docker.internal:8481']

- job_name: 'dynamic-routing-core'
  metrics_path: '/actuator/prometheus'
  static_configs:
    - targets: ['host.docker.internal:8085']

Metrics Queries

Gateway Error Rate

sum(rate(gateway_error_total[5m]))
/
sum(rate(gateway_requests_total[5m]))


Gateway Latency (P95)

histogram_quantile(
0.95,
sum by (le) (
rate(gateway_request_duration_seconds_bucket[5m])
)
)

Technology Stack

Java 17

Spring Boot 3

Spring Cloud Gateway

Spring WebFlux

Kafka

Redis

PostgreSQL

Eureka

Prometheus

Grafana

Zipkin

Docker (local infrastructure)

Project Status

The project is complete and considered production-ready as a showcase system.

It demonstrates architectural decisions, operational observability, and dynamic infrastructure patterns commonly used in real backend platforms.

Author
Rakhmonov Golibjon
Backend Engineer
Java, Spring, Cloud-native systems