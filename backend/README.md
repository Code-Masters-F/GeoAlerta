# GeoAlerta API

API RESTful que serve de ponte entre o aplicativo mobile (`android-app/`) e o
banco de dados descrito em [`docs/database-design`](../docs/database-design).

Construída **sem frameworks** (Spring): apenas **Servlets (Jakarta)**,
**Maven**, **JDBC** com **padrão DAO**, rodando em **Tomcat 10.1+**.

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

## Documentação interativa (Swagger / OpenAPI)

A API é descrita por um único contrato **OpenAPI 3.0** em
[`src/main/webapp/openapi.yaml`](src/main/webapp/openapi.yaml), servido junto
com a aplicação. A partir dele você tem **Swagger UI** e **Postman** sem manter
documentação duplicada.

Com a aplicação rodando:

| Recurso             | URL                                                  |
|---------------------|------------------------------------------------------|
| **Swagger UI**      | <http://localhost:8080/geoalerta-api/swagger.html>   |
| Contrato OpenAPI    | <http://localhost:8080/geoalerta-api/openapi.yaml>   |

No Swagger UI dá para ler todos os endpoints, schemas e exemplos, e usar o
botão **Try it out** para disparar requisições reais contra a API.

> O Swagger UI carrega seus assets via CDN (precisa de internet). O contrato
> `openapi.yaml` em si funciona offline.

### Importar no Postman

1. Postman → **Import** → arraste o arquivo `backend/src/main/webapp/openapi.yaml`
   (ou cole a URL `http://localhost:8080/geoalerta-api/openapi.yaml`).
2. O Postman gera automaticamente uma **Collection** com todas as rotas e
   exemplos de corpo.
3. Defina a variável de ambiente da collection `baseUrl` como
   `http://localhost:8080/geoalerta-api`.

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

## Plano de Testes

O plano cobre **testes manuais** da API (via `curl`, Swagger UI ou Postman) e o
**teste automatizado** já existente no app Android. Última execução completa:
**09/06/2026** — todos os casos aprovados.

**Pré-condições** (para os casos CT-01 a CT-08):

1. PostgreSQL no ar com `DDL.sql` e `DML.sql` carregados (seção
   [Configuração do banco](#configuração-do-banco));
2. API rodando em `http://localhost:8080/geoalerta-api` (`mvn cargo:run`).

### Casos de teste

| ID | Cenário | Entrada | Saída esperada | Status |
|----|---------|---------|----------------|--------|
| CT-01 | Health check com banco conectado | `GET /health` | `200` com `"bancoDeDados": "conectado"` | ✅ Aprovado |
| CT-02 | Listar alertas da carga inicial | `GET /alertas` | `200` com array JSON contendo os alertas do `DML.sql` | ✅ Aprovado |
| CT-03 | Criar alerta válido | `POST /alertas` com `{"nome": "Tempestade Litoral", "tipo": "Problemas climáticos", "grauGravidade": "Alta", "descricao": "Ventos acima de 90 km/h."}` | `201` com o alerta criado, `id` gerado e `dataDeEmissao` preenchida automaticamente | ✅ Aprovado |
| CT-04 | Rejeitar alerta sem campo obrigatório | `POST /alertas` com `{"tipo": "Problemas climáticos", "grauGravidade": "Alta"}` (sem `nome`) | `400` com `{"status": 400, "erro": "O campo 'nome' e obrigatorio"}` | ✅ Aprovado |
| CT-05 | Buscar alerta inexistente | `GET /alertas/9999` | `404` com `{"status": 404, "erro": "Alerta 9999 nao encontrado"}` | ✅ Aprovado |
| CT-06 | Cadastrar empresa válida | `POST /empresas` com `{"cnpj": "48.724.117/0001-90", "nomeFantasia": "Sitio Boa Vista", "email": "sitio@boavista.com.br", "senha": "segredo123"}` | `201` com CNPJ normalizado (só dígitos) e **sem** a senha/hash no corpo da resposta | ✅ Aprovado |
| CT-07 | Rejeitar CNPJ duplicado | Repetir o `POST /empresas` de CT-06 (ou usar o CNPJ `11.222.333/0001-81`, que já vem no `DML.sql`) | `409` com `{"status": 409, "erro": "Ja existe empresa com o CNPJ <cnpj>"}` | ✅ Aprovado |
| CT-08 | Rejeitar senha fraca | `POST /empresas` com senha `"123"` | `400` com `{"status": 400, "erro": "A senha deve ter ao menos 6 caracteres"}` | ✅ Aprovado |
| CT-09 | Validação de entrada do app (automatizado) | `./gradlew :app:testDebugUnitTest` em `android-app/` (roda o `InputValidatorTest`: CNPJ, e-mail, força de senha e sanitização) | `BUILD SUCCESSFUL` — 7 testes, 0 falhas | ✅ Aprovado |

> Massa criada pelos testes (alerta de CT-03 e empresa de CT-06) pode ser
> removida com `DELETE /alertas/{id}` e `DELETE /empresas/48724117000190`,
> deixando o banco no estado original.

### Como executar (passo a passo de 3 casos)

Com as pré-condições atendidas, os comandos abaixo são repetíveis e não exigem
preparação de massa. O `-w "\nHTTP %{http_code}\n"` imprime o status HTTP para
comparação com a saída esperada.

**CT-01 — Health check:**

```bash
curl -s -w "\nHTTP %{http_code}\n" http://localhost:8080/geoalerta-api/health
# Esperado: HTTP 200 e "bancoDeDados":"conectado"
```

**CT-04 — Validação de campo obrigatório:**

```bash
curl -s -w "\nHTTP %{http_code}\n" -X POST http://localhost:8080/geoalerta-api/alertas \
  -H 'Content-Type: application/json' \
  -d '{"tipo": "Problemas climáticos", "grauGravidade": "Alta"}'
# Esperado: HTTP 400 e {"erro":"O campo 'nome' e obrigatorio","status":400}
```

**CT-05 — Recurso inexistente:**

```bash
curl -s -w "\nHTTP %{http_code}\n" http://localhost:8080/geoalerta-api/alertas/9999
# Esperado: HTTP 404 e {"erro":"Alerta 9999 nao encontrado","status":404}
```

**CT-09 — Teste automatizado do app (bônus):**

```bash
cd android-app
./gradlew :app:testDebugUnitTest
# Esperado: BUILD SUCCESSFUL (7 testes, 0 falhas)
```

Os mesmos casos manuais podem ser executados pelo **Swagger UI** (botão *Try it
out* em <http://localhost:8080/geoalerta-api/swagger.html>) ou pelo **Postman**
(importando o `openapi.yaml`, como descrito acima).

### Onde coletar os resultados

| Fonte | O que fornece |
|-------|---------------|
| Saída do `curl` no terminal | Corpo JSON da resposta + código HTTP (com `-w "%{http_code}"`) para comparar com a saída esperada |
| Swagger UI (*Try it out*) | Código de status, corpo e headers de cada requisição na própria página |
| Postman (Collection Runner) | Histórico de execução e relatório exportável (JSON) da collection gerada pelo `openapi.yaml` |
| Console do `mvn cargo:run` | Logs do Tomcat/aplicação, incluindo stack traces de erros 500 |
| `android-app/app/build/reports/tests/testDebugUnitTest/index.html` | Relatório HTML do JUnit (CT-09), com cada teste, tempo e taxa de sucesso |
| `android-app/app/build/test-results/testDebugUnitTest/*.xml` | Resultados em XML (JUnit), úteis para integração com CI | 

## Integração com o app Android

O app (`android-app/`) hoje usa um `MockRepository`. Para consumir esta API basta
trocar a fonte de dados por chamadas REST (ex.: Retrofit) apontando para os
endpoints acima — os campos JSON usam `camelCase`, alinhados aos modelos do app.
