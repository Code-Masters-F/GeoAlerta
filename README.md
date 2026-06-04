# GeoAlerta

Plataforma B2B de monitoramento de risco climatico e agricola para o agronegocio
brasileiro. Acompanha propriedades em tempo real e emite alertas sobre secas,
chuvas excessivas, geadas, incendios e pragas antes que causem prejuizo.

## Stack

- Frontend: React 19 (JSX, sem TypeScript) + Vite
- Estilo: Tailwind CSS v4 + Framer Motion
- Icones: lucide-react
- Backend: planejado em Java (ainda nao integrado)

> Restricoes do projeto: sem TypeScript, sem Express e sem backend Node. O
> servidor sera implementado em Java posteriormente.

## Arquitetura

- `src/components/` — paginas (Landing, Login, Cadastro, Dashboard, Mapa,
  Propriedades, Preferencias) e layout.
- `src/components/ui/` — componentes reutilizaveis (Button, StatCard, AlertItem,
  RiskBar, PlanCard, PropertyCard, RiskTypeCard) e mapas de estilo.
- `src/data/` — fronteira de dados. Hoje retorna mock por meio de getters
  (`getPropriedades`, `getAlertasRecentes`, ...). Quando o backend Java estiver
  pronto, basta trocar o corpo de cada getter por uma chamada `fetch`, sem
  alterar os componentes.
- `src/index.css` — design system (tokens de cor, tipografia e efeitos).

## Como rodar

Pre-requisito: Node.js (apenas para o ambiente de desenvolvimento e build).

```bash
npm install
npm run dev      # http://localhost:3000
npm run build    # gera dist/
npm run preview  # serve a build de producao
```
