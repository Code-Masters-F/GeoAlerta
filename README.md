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

## Arquitetura do App

O projeto Android encontra-se na pasta `android-app/` e possui a seguinte estrutura de pacotes principal:

- `com.geoalerta.app.ui.views` — Telas (Views) da aplicação (ex: `LandingView`, `LoginView`, `DashboardView`, `MapView`, `PropertiesView`, etc.).
- `com.geoalerta.app.ui.components` — Componentes reaproveitáveis de UI (ex: `RiskBar`, `PropertyCard`, `StatCard`, navegação inferior `BottomNavigation`, etc.).
- `com.geoalerta.app.ui.theme` — Design System: Cores, Tipografia e definições do Material Theme.
- `com.geoalerta.app.models` — Fronteira de dados. Atualmente utiliza um `MockRepository` para devolver dados simulados nas Views (ex: Propriedades, Alertas, Riscos). Quando o backend Java estiver pronto, basta adaptar este repositório para realizar chamadas REST (ex: via Retrofit), sem quebrar a UI.
