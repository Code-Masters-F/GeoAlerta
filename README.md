# GeoAlerta - Global Solution 2026

Plataforma B2B de monitoramento de risco climático e agrícola para o agronegócio
brasileiro. Acompanha propriedades em tempo real e emite alertas sobre faltas de água grave,
chuvas excessivas, geadas, incêndios e pragas antes que causem prejuízo.

## Stack

- **Aplicativo (Mobile):** Android Nativo (Kotlin) + Jetpack Compose
- **Design System/Componentes:** Material Design 3
- **Mapas:** Google Maps SDK para Android (Maps Compose)
- **Analytics/Deploy:** Firebase Analytics e Firebase App Distribution
- **Backend:** planejado em Java (ainda não integrado, diretório `backend` preparado)

> **Nota de Atualização:** O projeto original foi planejado em React, porém a arquitetura foi inteiramente migrada para um aplicativo Android Nativo, garantindo melhor performance, integração facilitada com sensores mobile (ex: Localização via Google Maps) e melhor experiência do usuário (UX).

## Arquitetura do App

O projeto Android encontra-se na pasta `android-app/` e possui a seguinte estrutura de pacotes principal:

- `com.geoalerta.app.ui.views` — Telas (Views) da aplicação (ex: `LandingView`, `LoginView`, `DashboardView`, `MapView`, `PropertiesView`, etc.).
- `com.geoalerta.app.ui.components` — Componentes reaproveitáveis de UI (ex: `RiskBar`, `PropertyCard`, `StatCard`, navegação inferior `BottomNavigation`, etc.).
- `com.geoalerta.app.ui.theme` — Design System: Cores, Tipografia e definições do Material Theme.
- `com.geoalerta.app.models` — Fronteira de dados. Atualmente utiliza um `MockRepository` para devolver dados simulados nas Views (ex: Propriedades, Alertas, Riscos). Quando o backend Java estiver pronto, basta adaptar este repositório para realizar chamadas REST (ex: via Retrofit), sem quebrar a UI.

## Como rodar o aplicativo

Pré-requisitos:
- Android Studio (Iguana, Jellyfish, Koala ou superior)
- SDK Android API Nível 34
- Uma chave de API do Google Maps

1. Abra a pasta `android-app` no Android Studio.
2. Aguarde o **Gradle Sync** para baixar as dependências do Firebase, Compose e Maps.
3. Configure a sua chave do Maps no arquivo `local.properties` (crie na raiz do projeto se não existir):
   ```properties
   MAPS_API_KEY=SUA_CHAVE_AQUI
   ```
4. Selecione um emulador (API 24+) ou conecte um dispositivo físico via USB/Wi-Fi.
5. Clique em **Run 'app'** (`Shift + F10`) no painel superior.

Para gerar o APK:
- Para testes locais: vá em `Build > Build Bundle(s) / APK(s) > Build APK(s)`.
- O Firebase App Distribution pode ser utilizado para compartilhar builds fechadas com a equipe.
