<center>  
  <h1 align="center">🚀 Microserviço: Administração do Catálogo de Vídeos - Kotlin</h1>
  <p align="center">
    Microserviço referente ao backend da Administração do Catálogo de Vídeos<br />
    Utilizando Kotlin, Clean Architecture, DDD, TDD e as boas práticas atuais de mercado
  </p>
</center>
<br />

## Entidade Categoria

### Campos
- Nome
- Descrição
- Ativar ou desativar
- Auditoria (data de criação, atualização e deleção)

### Regras de Negócio
- Nome não deve ser nulo ou em branco
- Nome deve conter no mínimo 3 caracteres
- Nome deve conter no máximo 255 caracteres
- Descrição pode ser nula ou em branco

## Entidade Gênero

### Campos
- Nome
- Relação N para N com Categorias
- Ativar ou desativar
- Auditoria (data de criação, atualização e deleção)

### Regras de Negócio
- Nome não deve ser nulo ou em branco
- Nome deve conter no máximo 255 caracteres

## Entidade Cast Member (Elenco)

### Campos
- Nome
- Tipo
- Auditoria (data de criação e atualização)

### Regras de Negócio
- Nome não deve ser nulo ou em branco
- Nome deve conter no mínimo 3 caracteres
- Nome deve conter no máximo 255 caracteres

## Ferramentas necessárias

- JDK 17
- IDE de sua preferência
- Docker

## Como executar?

1. Clonar o repositório:
```sh
git clone https://github.com/Lukinhasssss/admin-catalogo-de-videos-kotlin.git
```

2. Subir o banco de dados PostgreSQL com Docker:
```shell
docker-compose up -d
```

3. Executar as migrações do PostgreSQL com o Flyway:
```shell
./gradlew flywayMigrate
```

4. Executar a aplicação como SpringBoot app:
```shell
./gradlew bootRun
``` 

> Também é possível executar como uma aplicação Java através do
> método main() na classe Main.java
## Banco de dados

O banco de dados principal é um PostgreSQL e para subir localmente vamos utilizar o
Docker. Execute o comando a seguir para subir o PostgreSQL:

```shell
docker-compose up -d
```

Pronto! Aguarde que em instantes o PostgreSQL irá estar pronto para ser consumido
na porta 5432.

### Migrações do banco de dados com Flyway

#### Executar as migrações

Caso seja a primeira vez que esteja subindo o banco de dados, é necessário
executar as migrações SQL com a ferramenta `flyway`.
Execute o comando a seguir para executar as migrações:

```shell
./gradlew flywayMigrate
```

Pronto! Agora sim o banco de dados PostgreSQL está pronto para ser utilizado.

<br/>

#### Limpar as migrações do banco

É possível limpar (deletar todas as tabelas) seu banco de dados, basta
executar o seguinte comando:

```shell
./gradlew flywayClean
```

MAS lembre-se: "Grandes poderes, vem grandes responsabilidades".

<br/>

#### Reparando as migrações do banco

Existe duas maneiras de gerar uma inconsistência no Flyway deixando ele no estado de reparação:

1. Algum arquivo SQL de migração com erro;
2. Algum arquivo de migração já aplicado foi alterado (modificando o `checksum`).

Quando isso acontecer o flyway ficará em um estado de reparação
com um registro na tabela `flyway_schema_history` com erro (`sucesso = 0`).

Para executar a reparação, corrija os arquivos e execute:
```shell
./gradlew flywayRepair
```

Com o comando acima o Flyway limpará os registros com erro da tabela `flyway_schema_history`,
na sequência execute o comando FlywayMigrate para tentar migrar-los novamente.

<br/>

#### Outros comandos úteis do Flyway

Além dos comandos já exibidos, temos alguns outros muito úteis como o info e o validate:

```shell
./gradlew flywayInfo
./gradlew flywayValidate
```

Para saber todos os comandos disponíveis: [Flyway Gradle Plugin](https://flywaydb.org/documentation/usage/gradle/info)

<br/>

#### Para executar os comandos em outro ambiente

Lá no `build.gradle` configuramos o Flyway para lêr primeiro as variáveis de
ambiente `FLYWAY_DB`, `FLYWAY_USER` e `FLYWAY_PASS` e depois usar um valor padrão
caso não as encontre. Com isso, para apontar para outro ambiente basta sobrescrever
essas variáveis na hora de executar os comandos, exemplo:

```shell
FLYWAY_DB=jdbc:postgresql://prod:5432/adm_videos FLYWAY_USER=root FLYWAY_PASS=123h1hu ./gradlew flywayValidate
```