# GeoAlerta - Global Solution 2026

Plataforma B2B de monitoramento de risco climático e agrícola para o agronegócio
brasileiro. Acompanha propriedades em tempo real e emite alertas sobre falta de água grave,
chuvas excessivas, geadas, incêndios e pragas antes que causem prejuízo.

O projeto é composto por um **app Android nativo** (Kotlin + Jetpack Compose), uma
**API REST em Java** (Servlets/Jakarta sobre Tomcat) e um **banco PostgreSQL**. A
API e o banco já estão preparados, mas **ainda não foram integrados** ao app, que
hoje roda com dados simulados (`MockRepository`).

📄 **Para mais informações sobre o projeto**, consulte a
[documentação no Google Docs](https://docs.google.com/document/d/1-IYuo7pOO2MuyuZBWJFNNVva1Imb4HRRDlxVllIIWKA/edit?usp=sharing).
Detalhes específicos de cada parte estão em:

- App Android → [`android-app/`](android-app/) (arquitetura no fim deste arquivo)
- API REST → [`backend/README.md`](backend/README.md)
- Banco de dados → [`docs/database-design/`](docs/database-design)

## Stack

- **Aplicativo (Mobile):** Android Nativo (Kotlin) + Jetpack Compose
- **Design System/Componentes:** Material Design 3
- **Mapas:** Google Maps SDK para Android (Maps Compose)
- **Analytics/Deploy:** Firebase Analytics e Firebase App Distribution
- **Backend:** Java + Servlets (Jakarta), Maven, JDBC/DAO, Tomcat 10.1+ (preparado, ainda não integrado ao app)
- **Banco:** PostgreSQL

---

## 🚀 Quick Start

Para rodar o projeto de ponta a ponta você sobe o **banco**, depois a **API** e,
por fim, o **app Android**. Cada parte pode ser executada de forma independente.

### Pré-requisitos

- **JDK 17+** e **Maven 3.9+** (backend)
- **PostgreSQL 16** — local ou via Docker (banco)
- **Android Studio** (Iguana ou superior) + **SDK Android API 34** (app)
- Uma **chave de API do Google Maps** (app)

### 1. Banco de dados

Suba um PostgreSQL e carregue o schema + dados de exemplo. A forma mais rápida é
via Docker:

```bash
docker run -d --name geoalerta-pg -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=postgres -p 5432:5432 postgres:16-alpine

psql postgresql://postgres:postgres@localhost:5432/postgres -f docs/database-design/DDL.sql
psql postgresql://postgres:postgres@localhost:5432/postgres -f docs/database-design/DML.sql
```

### 2. Backend (API REST)

```bash
cd backend
cp .env.example .env   # ajuste as credenciais do banco, se necessário
mvn cargo:run          # baixa um Tomcat 10.1 e sobe a API em http://localhost:8080
```

Teste rápido:

```bash
curl http://localhost:8080/geoalerta-api/health
curl http://localhost:8080/geoalerta-api/alertas
```

A documentação interativa fica em
<http://localhost:8080/geoalerta-api/swagger.html> (Swagger UI). Para variáveis de
ambiente do banco, deploy em Tomcat já existente e todos os endpoints, veja o
[README do backend](backend/README.md).

### 3. App Android

1. Abra a pasta `android-app` no Android Studio e aguarde o **Gradle Sync**.
2. Configure sua chave do Maps no arquivo `local.properties` (na raiz de
   `android-app/`, crie se não existir):
   ```properties
   MAPS_API_KEY=SUA_CHAVE_AQUI
   ```
3. Selecione um emulador (API 24+) ou conecte um dispositivo físico.
4. Clique em **Run 'app'** (`Shift + F10`).

> O app hoje usa um `MockRepository` com dados simulados e **ainda não está
> integrado** ao backend, então roda sem a API. Quando a integração for feita,
> basta trocar a fonte de dados por chamadas REST (ex.: Retrofit) apontando para
> os endpoints acima.

Para gerar o APK: `Build > Build Bundle(s) / APK(s) > Build APK(s)`.

---

## 🔐 Autenticação e Práticas de Segurança

O app possui **login e cadastro de empresas com senha criptografada**, usando
dados mockados em memória (sem consumo de API), no pacote
[`android-app/app/src/main/java/com/geoalerta/app/auth/`](android-app/app/src/main/java/com/geoalerta/app/auth/).

**Conta demo:** CNPJ `12.345.678/0001-95` · senha `GeoAlerta2026`

### Onde cada prática foi implementada

| Prática | Arquivo | Como funciona |
|---|---|---|
| **Criptografia de senha (hash + salt)** | [`auth/PasswordHasher.kt`](android-app/app/src/main/java/com/geoalerta/app/auth/PasswordHasher.kt) | As senhas nunca são armazenadas em texto puro. Cada senha vira um hash **PBKDF2 (HMAC-SHA256, 60.000 iterações)** com **salt aleatório de 16 bytes** (`SecureRandom`), guardado no formato `algoritmo$iterações$salt$hash`. O salt garante que senhas iguais gerem hashes diferentes; as iterações encarecem ataques de força bruta. |
| **Validação de entrada** | [`auth/InputValidator.kt`](android-app/app/src/main/java/com/geoalerta/app/auth/InputValidator.kt) | Antes de autenticar/cadastrar, valida: **CNPJ** (14 dígitos + dígitos verificadores pelo algoritmo oficial módulo 11), **e-mail** (regex), **força da senha** (mínimo 8 caracteres com maiúscula, minúscula e número) e **tamanho máximo** dos campos. |
| **Proteção contra SQLi/XSS (sanitização)** | [`auth/InputValidator.kt`](android-app/app/src/main/java/com/geoalerta/app/auth/InputValidator.kt) (`textoSeguro`) e nas telas [`ui/views/LoginView.kt`](android-app/app/src/main/java/com/geoalerta/app/ui/views/LoginView.kt) / [`ui/views/CadastroView.kt`](android-app/app/src/main/java/com/geoalerta/app/ui/views/CadastroView.kt) | Campos de texto livre rejeitam caracteres típicos de payloads de injeção (`<`, `>`, `'`, `"`, `;`, `` ` ``, `\`, `--`); o campo de CNPJ filtra a entrada na origem, aceitando só dígitos e máscara. Assim, mesmo quando o app for integrado ao backend, nenhum dado malicioso parte do formulário. |
| **Verificação em tempo constante + mensagem genérica** | [`auth/PasswordHasher.kt`](android-app/app/src/main/java/com/geoalerta/app/auth/PasswordHasher.kt) e [`auth/AuthRepository.kt`](android-app/app/src/main/java/com/geoalerta/app/auth/AuthRepository.kt) | A comparação do hash usa `MessageDigest.isEqual` (tempo constante, evita *timing attacks*) e o erro de login é genérico ("CNPJ ou senha incorretos"), sem revelar se o CNPJ existe — evita enumeração de contas. |

O armazenamento dos usuários é mockado em
[`auth/AuthRepository.kt`](android-app/app/src/main/java/com/geoalerta/app/auth/AuthRepository.kt)
(lista em memória, no mesmo padrão do `MockRepository`). Os testes unitários da
validação estão em
[`app/src/test/java/com/geoalerta/app/auth/InputValidatorTest.kt`](android-app/app/src/test/java/com/geoalerta/app/auth/InputValidatorTest.kt)
(`./gradlew :app:testDebugUnitTest`).

---

## Arquitetura do App

O projeto Android encontra-se na pasta `android-app/` e possui a seguinte estrutura de pacotes principal:

- `com.geoalerta.app.ui.views` — Telas (Views) da aplicação (ex: `LandingView`, `LoginView`, `DashboardView`, `MapView`, `PropertiesView`, etc.).
- `com.geoalerta.app.ui.components` — Componentes reaproveitáveis de UI (ex: `RiskBar`, `PropertyCard`, `StatCard`, navegação inferior `BottomNavigation`, etc.).
- `com.geoalerta.app.ui.theme` — Design System: Cores, Tipografia e definições do Material Theme.
- `com.geoalerta.app.auth` — Autenticação mockada: hash de senha (`PasswordHasher`), validação/sanitização de entrada (`InputValidator`) e repositório de empresas (`AuthRepository`). Ver seção [Autenticação e Práticas de Segurança](#-autenticação-e-práticas-de-segurança).
- `com.geoalerta.app.models` — Fronteira de dados. Atualmente utiliza um `MockRepository` para devolver dados simulados nas Views (ex: Propriedades, Alertas, Riscos). Quando o backend Java estiver pronto, basta adaptar este repositório para realizar chamadas REST (ex: via Retrofit), sem quebrar a UI.
