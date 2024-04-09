printf "Criando os docker volumes...\n"
docker volume create postgresql-admin-do-catalogo
docker volume create rabbitmq-admin-do-catalogo
docker volume create keycloak-admin-do-catalogo
docker volume create grafana-data-codeflix
docker volume create prometheus-data-codeflix
docker volume create elasticsearch-admin-do-catalogo
docker volume create filebeat-admin-do-catalogo

# ----------------------------------------------------

printf "Criando as docker networks...\n"
docker network create admin-do-catalogo
docker network create admin-do-catalogo-services
docker network create monitoramento-codeflix
docker network create open-telemetry-codeflix
docker network create elasticsearch-codeflix
docker network create catalogo-de-videos


# ----------------------------------------------------

printf "Inicializando os container...\n"
docker compose -f services/docker-compose.yml up -d
docker compose -f elk/docker-compose.yml up -d
docker compose -f monitoramento/docker-compose.yml up -d
docker compose -f open-telemetry/docker-compose.yml up -d
docker compose -f app/docker-compose.yml up -d --build --force-recreate
#docker compose -f app/docker-compose.yml up -d
