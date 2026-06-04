/**
 * Dados do painel principal (Dashboard).
 * Mock hoje; trocar os getters por chamadas ao backend Java depois.
 */

const indicadores = [
  { id: 'propriedades', valor: '4', titulo: 'Propriedades', legenda: 'monitoradas', icone: 'tractor', tom: 'primary' },
  { id: 'alertas', valor: '5', titulo: 'Alertas Ativos', legenda: 'nas ultimas 48h', icone: 'bell', tom: 'warning' },
  { id: 'criticos', valor: '1', titulo: 'Nivel Critico', legenda: 'requer acao', icone: 'alert-triangle', tom: 'error' },
  { id: 'culturas', valor: '4', titulo: 'Culturas', legenda: 'soja, milho, cafe...', icone: 'sprout', tom: 'success' },
];

const alertasRecentes = [
  {
    id: 'a1',
    titulo: 'Incendio Rural',
    local: 'Setor B-04',
    prioridade: 'Alta Prioridade',
    severidade: 'critico',
    icone: 'flame',
  },
  {
    id: 'a2',
    titulo: 'Seca / Deficit Hidrico',
    local: 'Setor A-12',
    prioridade: 'Monitoramento',
    severidade: 'atencao',
    icone: 'droplets',
  },
];

const probabilidades = [
  { id: 'incendio', nome: 'Incendio Rural', valor: 91, severidade: 'critico' },
  { id: 'seca', nome: 'Seca / Hidrico', valor: 78, severidade: 'atencao' },
  { id: 'geada', nome: 'Geada', valor: 62, severidade: 'normal' },
  { id: 'pragas', nome: 'Pragas', valor: 53, severidade: 'normal' },
];

export function getIndicadores() {
  return indicadores;
}

export function getAlertasRecentes() {
  return alertasRecentes;
}

export function getProbabilidadesDeRisco() {
  return probabilidades;
}
