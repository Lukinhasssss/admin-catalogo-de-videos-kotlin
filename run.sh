printf "Criando os docker volumes...\n"
docker volume create postgresql-admin-do-catalogo
docker volume create rabbitmq-admin-do-catalogo
docker volume create keycloak-codeflix
docker volume create postgresql-keycloak-codeflix
docker volume create grafana-data-codeflix
docker volume create prometheus-data-codeflix
docker volume create elasticsearch-codeflix
docker volume create filebeat-codeflix

# ----------------------------------------------------

printf "Criando as docker networks...\n"
docker network create admin-do-catalogo
docker network create catalogo-de-videos
docker network create postgresql-admin-do-catalogo
docker network create rabbitmq-admin-do-catalogo
docker network create keycloak-codeflix
docker network create monitoramento-codeflix
docker network create open-telemetry-codeflix
docker network create elasticsearch-codeflix

# ----------------------------------------------------

printf "Inicializando os container...\n"
docker compose -f docker-compose-codeflix.yaml up -d
docker compose -f docker-compose.yaml up -d
#docker compose -f docker-compose.yaml up -d --build --force-recreate