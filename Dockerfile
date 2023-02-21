FROM eclipse-temurin:17.0.6_10-jre-alpine

WORKDIR /app

ARG JAR_FILE=build/libs/app*.jar

COPY $JAR_FILE /app.jar
COPY --from=docker.elastic.co/observability/apm-agent-java:1.34.0 /usr/agent/elastic-apm-agent.jar /apm-agent.jar
COPY opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ENTRYPOINT [ "java", \
             "-javaagent:/apm-agent.jar", \
             "-Delastic.apm.service_name=admin-do-catalogo", \
             "-Delastic.apm.server_url=http://apm-admin-do-catalogo:8200", \
             "-Delastic.apm.application_packages=com.lukinhasssss", \
             "-javaagent:/opentelemetry-javaagent.jar", \
             "-Dotel.service.name=admin-do-catalogo", \
             "-Dotel.traces.exporter=otlp", \
             "-Dotel.metrics.exporter=otlp", \
             "-Dotel.integration.jdbc.datasource.enabled=true", \
             "-Dotel.instrumentation.jdbc.datasource.enabled=true", \
             "-Dotel.exporter.otlp.endpoint=http://collector-admin-do-catalogo:4318", \
             "-Dotel.exporter.otlp.protocol=http/protobuf", \
             "-jar", "/app.jar" \
]

#CMD java -javaagent:/apm-agent.jar -Delastic.apm.service_name=admin-do-catalogo -Delastic.apm.server_url=http://apm-admin-do-catalogo:8200 -Delastic.apm.application_packages=com.lukinhasssss -javaagent:/opentelemetry-javaagent.jar -Dotel.service.name=admin-do-catalogo -Dotel.traces.exporter=otlp -Dotel.metrics.exporter=otlp -Dotel.integration.jdbc.datasource.enabled=true -Dotel.instrumentation.jdbc.datasource.enabled=true -Dotel.exporter.otlp.endpoint=http://collector-admin-do-catalogo:4318 -Dotel.exporter.otlp.protocol=http/protobuf -jar /app.jar
