printf "Criando as pastas com permissões...\n"
sudo chown root app/filebeat/filebeat.yaml
mkdir -m 777 .docker
mkdir -m 777 .docker/postgresql
mkdir -m 777 .docker/rabbitmq
mkdir -m 777 .docker/keycloak
mkdir -m 777 .docker/filebeat
mkdir -m 777 .docker/elasticsearch-admin-do-catalogo

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
docker compose -f app/docker-compose.yml up -d
#docker compose -f app/docker-compose.yml up -d --build --force-recreate
