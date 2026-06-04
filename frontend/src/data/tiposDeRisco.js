/**
 * Tipos de risco monitorados pela GeoAlerta.
 *
 * Fronteira de dados: hoje os valores sao estaticos (mock). Quando o backend
 * Java estiver disponivel, basta trocar o corpo de `getTiposDeRisco` por uma
 * chamada `fetch('/api/riscos')` mantendo o mesmo formato de retorno.
 */

const tiposDeRisco = [
  {
    id: 'seca',
    nome: 'Seca / Deficit Hidrico',
    icone: 'thermometer-sun',
    tom: 'warning',
    nivel: 'essencial',
    descricao:
      'Principal causador de perda agricola no Brasil. Monitoramos indices de umidade e NDVI em tempo real.',
  },
  {
    id: 'chuva',
    nome: 'Chuva Excessiva',
    icone: 'cloud-rain',
    tom: 'info',
    nivel: 'essencial',
    descricao:
      'Detecta alagamentos que prejudicam plantio, colheita, solo e raizes antes que se intensifiquem.',
  },
  {
    id: 'calor',
    nome: 'Onda de Calor',
    icone: 'thermometer-sun',
    tom: 'error',
    nivel: 'essencial',
    descricao:
      'Monitora estresse termico e evapotranspiracao para proteger a produtividade da sua cultura.',
  },
  {
    id: 'geada',
    nome: 'Geada',
    icone: 'snowflake',
    tom: 'info',
    nivel: 'avancado',
    descricao:
      'Antecipa quedas bruscas de temperatura que comprometem culturas sensiveis como cafe e citros.',
  },
  {
    id: 'incendio',
    nome: 'Incendio Rural',
    icone: 'flame',
    tom: 'error',
    nivel: 'avancado',
    descricao:
      'Cruza focos de calor por satelite com clima seco para alertar sobre risco de fogo na propriedade.',
  },
  {
    id: 'pragas',
    nome: 'Pragas e Doencas',
    icone: 'bug',
    tom: 'success',
    nivel: 'avancado',
    descricao:
      'Identifica condicoes favoraveis a surtos de pragas a partir de anomalias de biomassa e clima.',
  },
];

export function getTiposDeRisco() {
  return tiposDeRisco;
}
