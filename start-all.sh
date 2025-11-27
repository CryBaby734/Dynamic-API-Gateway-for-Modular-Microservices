#!/bin/bash
echo "🚀 Starting full Spring Cloud environment..."

# === 1️⃣ Start Docker services ===

# Zipkin
if [ "$(docker ps -q -f name=zipkin)" ]; then
  echo "✅ Zipkin is already running"
else
  echo "🟢 Starting Zipkin..."
  docker start zipkin 2>/dev/null || docker run -d --name zipkin -p 9411:9411 openzipkin/zipkin
fi

# RabbitMQ
if [ "$(docker ps -q -f name=rabbitmq)" ]; then
  echo "✅ RabbitMQ is already running"
else
  echo "🟢 Starting RabbitMQ..."
  docker start rabbitmq 2>/dev/null || docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
fi

# Prometheus
PROM_PATH="/Users/mac/IdeaProjects/SpringCloudMastery/prometheus.yml"
if [ "$(docker ps -q -f name=prometheus)" ]; then
  echo "✅ Prometheus is already running"
else
  echo "🟢 Starting Prometheus..."
  docker start prometheus 2>/dev/null || docker run -d \
    -p 9090:9090 \
    -v "$PROM_PATH":/etc/prometheus/prometheus.yml \
    --name prometheus prom/prometheus
fi

# Grafana
if [ "$(docker ps -q -f name=grafana)" ]; then
  echo "✅ Grafana is already running"
else
  echo "🟢 Starting Grafana..."
  docker start grafana 2>/dev/null || docker run -d -p 3000:3000 --name grafana grafana/grafana
fi

# === 2️⃣ Start Spring Boot microservices ===
echo "⚙️ Starting Spring Boot services..."

osascript <<EOF
tell application "Terminal"
    do script "cd ~/IdeaProjects/SpringCloudMastery && ./gradlew :config-server:bootRun"
    do script "cd ~/IdeaProjects/SpringCloudMastery && ./gradlew :discovery-server:bootRun"
    do script "cd ~/IdeaProjects/SpringCloudMastery && ./gradlew :gateway-service:bootRun"
    do script "cd ~/IdeaProjects/SpringCloudMastery && ./gradlew :spring-cloud-mastery:bootRun"
    do script "cd ~/IdeaProjects/SpringCloudMastery && ./gradlew :user-service:bootRun"
end tell
EOF

echo ""
echo "✅ All services are starting!"
echo "🌍 Eureka → http://localhost:8761"
echo "🐇 RabbitMQ → http://localhost:15672"
echo "🧩 Zipkin → http://localhost:9411"
echo "📈 Prometheus → http://localhost:9090"
echo "📊 Grafana → http://localhost:3000"
echo "🚪 Gateway → http://localhost:8481"