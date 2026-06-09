#!/bin/bash
echo "Injecting Unlimited Route..."
curl -X POST http://localhost:8085/routes \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": "perf-unlimited",
    "enabled": true,
    "uri": "lb://USER-SERVICE",
    "predicates": [
      {
        "name": "Path",
        "args": { "_genkey_0": "/api/perf/unlimited/**" }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": { "parts": "1" }
      }
    ]
  }'
echo -e "\n"

echo "Injecting Rate-Limited Route (100 RPS)..."
curl -X POST http://localhost:8085/routes \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": "perf-limited",
    "enabled": true,
    "uri": "lb://USER-SERVICE",
    "predicates": [
      {
        "name": "Path",
        "args": { "_genkey_0": "/api/perf/limited/**" }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": { "parts": "1" }
      },
      {
        "name": "RequestRateLimiter",
        "args": {
          "redis-rate-limiter.replenishRate": "100",
          "redis-rate-limiter.burstCapacity": "100",
          "redis-rate-limiter.requestedTokens": "1"
        }
      }
    ]
  }'
echo -e "\n"
