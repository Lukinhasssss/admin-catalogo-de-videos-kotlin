FROM openjdk:17-alpine

WORKDIR /app

ARG JAR_FILE=build/libs/app*.jar

#ENV SPRING_PROFILES_ACTIVE="prod"
#ENV DB_POSTGRES_URL="postgres-admin-do-catalogo"
#ENV DB_POSTGRES_USERNAME="lukinhasssss"
#ENV DB_POSTGRES_PASSWORD="348t7y30549g4qptbq4rtbq4b5rq3rvq34rfq3784yq23847yqor78hvgoreiuvn"
#ENV DB_POSTGRES_SCHEMA="adm_videos"
#ENV FLYWAY_DB="jdbc:postgresql://postgres-admin-do-catalogo:5432/adm_videos"
#ENV FLYWAY_USER="lukinhasssss"
#ENV FLYWAY_PASSWORD="348t7y30549g4qptbq4rtbq4b5rq3rvq34rfq3784yq23847yqor78hvgoreiuvn"

COPY ${JAR_FILE} /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]