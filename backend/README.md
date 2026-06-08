# GeoAlerta API

API RESTful que serve de ponte entre o aplicativo mobile (`android-app/`) e o
banco de dados PostgreSQL descrito em [`docs/database-design`](../docs/database-design).

Construída **sem frameworks "pesados"** (sem Spring): apenas **Servlets (Jakarta)**,
**Maven**, **JDBC** com **padrão DAO** e **PostgreSQL**, rodando em **Tomcat 10.1+**.

## Arquitetura (MVC em camadas)

A requisição percorre quatro camadas bem separadas:

```
HTTP  ─►  Controller  ─►  Service  ─►  Repository (DAO)  ─►  PostgreSQL
(Servlet)            (regras/validação)   (SQL/JDBC)
```

| Camada         | Pacote                              | Responsabilidade                                   |
|----------------|-------------------------------------|----------------------------------------------------|
| **Controller** | `com.geoalerta.api.controller`      | Servlets: roteamento HTTP, (de)serialização JSON   |
| **Service**    | `com.geoalerta.api.service`         | Regras de negócio e validações                     |
| **Repository** | `com.geoalerta.api.repository`      | DAO: acesso a dados via JDBC/`PreparedStatement`   |
| **Model**      | `com.geoalerta.api.model`           | Entidades (POJOs) e DTOs de entrada                |
| Infra/Utils    | `com.geoalerta.api.config` / `util` | Conexão, JSON, hashing de senha, erros             |

## Endpoints

Context path padrão: **`/geoalerta-api`**. Todos respondem JSON (UTF-8) e CORS habilitado.

| Método   | Rota                  | Descrição                       |
|----------|-----------------------|---------------------------------|
| `GET`    | `/health`             | Status da API e do banco        |
| `GET`    | `/alertas`            | Lista alertas                   |
| `GET`    | `/alertas/{id}`       | Busca alerta por id             |
| `POST`   | `/alertas`            | Cria alerta                     |
| `PUT`    | `/alertas/{id}`       | Atualiza alerta                 |
| `DELETE` | `/alertas/{id}`       | Remove alerta                   |
| `GET`    | `/empresas`           | Lista empresas                  |
| `GET`    | `/empresas/{cnpj}`    | Busca empresa por CNPJ          |
| `POST`   | `/empresas`           | Cadastra empresa                |
| `PUT`    | `/empresas/{cnpj}`    | Atualiza empresa                |
| `DELETE` | `/empresas/{cnpj}`    | Remove empresa                  |
| `GET`    | `/sensores`           | Lista sensores                  |
| `GET`    | `/sensores/{id}`      | Busca sensor por id             |
| `POST`   | `/sensores`           | Cadastra sensor                 |
| `PUT`    | `/sensores/{id}`      | Atualiza sensor                 |
| `DELETE` | `/sensores/{id}`      | Remove sensor                   |

Respostas de erro seguem o formato `{"status": <int>, "erro": "<mensagem>"}`
(400 validação, 404 não encontrado, 409 conflito, 500 erro de banco).

### Exemplos de corpo (JSON)

```jsonc
// POST /alertas
{ "nome": "Tempestade Litoral", "tipo": "Problemas climáticos",
  "grauGravidade": "Alta", "dataDeEmissao": "2026-05-10T06:30:00",
  "descricao": "Ventos acima de 90 km/h." }

// POST /empresas  (a senha é convertida em hash e NUNCA é devolvida)
{ "cnpj": "11.222.333/0001-81", "nomeFantasia": "Fazenda São João",
  "email": "contato@fazenda.com.br", "senha": "segredo123" }

// POST /sensores  (status: ATIVO | SUSPENSO | DESATIVADO)
{ "tipo": "Estacao meteorologica", "erdPlusCode": "588MC9X8+5R",
  "status": "ATIVO", "dataInstalacao": "2025-11-05T09:00:00" }
```

## Configuração do banco

A configuração é resolvida nesta ordem de precedência: **variável de ambiente
→ propriedade de sistema (`-Dchave=valor`) → arquivo `.env` → padrão**.

| Variável                | Padrão                                         |
|-------------------------|------------------------------------------------|
| `GEOALERTA_DB_URL`      | `jdbc:postgresql://localhost:5432/postgres`    |
| `GEOALERTA_DB_USER`     | `postgres`                                     |
| `GEOALERTA_DB_PASSWORD` | `postgres`                                     |

A forma mais simples é copiar o modelo e ajustar os valores (o `.env` não é
versionado):

```bash
cd backend
cp .env.example .env      # edite .env com os dados do seu banco
```

O `.env` é procurado a partir do diretório de execução, subindo alguns níveis;
também é possível apontar um caminho específico com `GEOALERTA_ENV_FILE`.

Crie o schema e os dados de exemplo a partir de `docs/database-design`:

```bash
psql "$GEOALERTA_DB_URL" -f ../docs/database-design/DDL.sql
psql "$GEOALERTA_DB_URL" -f ../docs/database-design/DML.sql
```

## Como rodar

Pré-requisitos: **JDK 17+** e **Maven 3.9+**.

### Opção 1 — Tomcat embarcado (desenvolvimento)

```bash
cd backend
mvn cargo:run        # baixa um Tomcat 10.1 e sobe a app em http://localhost:8080
```

Teste rápido:

```bash
curl http://localhost:8080/geoalerta-api/health
curl http://localhost:8080/geoalerta-api/alertas
```

### Opção 2 — WAR em um Tomcat existente (produção)

```bash
cd backend
mvn clean package                       # gera target/geoalerta-api.war
cp target/geoalerta-api.war "$CATALINA_HOME/webapps/"
```

> Requer **Tomcat 10.1+** (namespace `jakarta.*`). Para Tomcat 9 (namespace
> `javax.*`) seria necessário trocar a dependência `jakarta.servlet-api`.

### Subir um PostgreSQL de teste com Docker

```bash
docker run -d --name geoalerta-pg -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=postgres -p 5432:5432 postgres:16-alpine
psql postgresql://postgres:postgres@localhost:5432/postgres -f ../docs/database-design/DDL.sql
psql postgresql://postgres:postgres@localhost:5432/postgres -f ../docs/database-design/DML.sql
```

## Integração com o app Android

O app (`android-app/`) hoje usa um `MockRepository`. Para consumir esta API basta
trocar a fonte de dados por chamadas REST (ex.: Retrofit) apontando para os
endpoints acima — os campos JSON usam `camelCase`, alinhados aos modelos do app.
