FROM openjdk:17-alpine

WORKDIR /app

ARG JAR_FILE=build/libs/app*.jar

COPY ${JAR_FILE} /app.jar
COPY --from=docker.elastic.co/observability/apm-agent-java:1.34.0 /usr/agent/elastic-apm-agent.jar /apm-agent.jar

ENTRYPOINT [ "java", \
             "-javaagent:/apm-agent.jar", \
             "-Delastic.apm.service_name=admin-do-catalogo", \
             "-Delastic.apm.server_url=http://apm-admin-do-catalogo:8200", \
             "-Delastic.apm.application_packages=com.lukinhasssss", \
             "-jar", "/app.jar" \
]
