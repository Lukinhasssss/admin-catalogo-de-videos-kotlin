# To build and run:

# build stage
FROM gradle:8.5-jdk21-alpine AS builder

WORKDIR /app

COPY . .

RUN gradle bootJar

# build runtime
FROM eclipse-temurin:21-jre-alpine

ARG JAR_FILE=/app/build/libs/app*.jar

COPY --from=builder $JAR_FILE /app.jar

# Download do OpenTelemetry Java Agent
#RUN wget -O /opentelemetry-javaagent.jar https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.31.0/opentelemetry-javaagent.jar
RUN wget -O /opentelemetry-javaagent.jar https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar

# Download do Elastic APM Java Agent
#RUN wget -O /apm-agent.jar https://repo1.maven.org/maven2/co/elastic/apm/elastic-apm-agent/1.46.0/elastic-apm-agent-1.46.0.jar
COPY --from=docker.elastic.co/observability/apm-agent-java:latest /usr/agent/elastic-apm-agent.jar /apm-agent.jar

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ENTRYPOINT [ "java", \
             "-javaagent:/apm-agent.jar", \
             "-Delastic.apm.service_name=admin-do-catalogo", \
             "-Delastic.apm.server_url=http://apm-codeflix:8200", \
             "-Delastic.apm.environment=codeflix", \
             "-Delastic.apm.application_packages=com.lukinhasssss", \
             "-javaagent:/opentelemetry-javaagent.jar", \
             "-Dotel.service.name=admin-do-catalogo", \
             "-Dotel.exporter.otlp.endpoint=http://otel-collector-codeflix:4318", \
             "-Dotel.exporter.otlp.protocol=http/protobuf", \
             "-jar", "/app.jar" \
]