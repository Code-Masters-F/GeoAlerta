/**
 * Planos comerciais exibidos na landing page.
 * Mock hoje; trocar `getPlanos` por chamada ao backend depois.
 */

const planos = [
  {
    id: 'essencial',
    nome: 'Essencial',
    descricao: 'Alertas basicos para pequenos produtores.',
    preco: 'R$ 79',
    periodo: '/mes',
    destaque: false,
    cta: 'Comecar com Essencial',
    beneficios: [
      'Cadastro de ate 1 propriedade',
      '3 tipos de risco essenciais',
      'Alertas por e-mail',
    ],
  },
  {
    id: 'profissional',
    nome: 'Profissional',
    descricao: 'Visibilidade completa dos riscos.',
    preco: 'R$ 349',
    periodo: '/mes',
    destaque: true,
    cta: 'Comecar com Profissional',
    beneficios: [
      'Tudo do Essencial',
      'Ate 10 propriedades',
      '7 tipos de risco monitorados',
      'Alertas por push e WhatsApp',
    ],
  },
  {
    id: 'enterprise',
    nome: 'Enterprise',
    descricao: 'Para grandes fazendas e grupos.',
    preco: 'Sob consulta',
    periodo: '',
    destaque: false,
    cta: 'Falar com especialista',
    beneficios: [
      'Tudo do Profissional',
      'Propriedades ilimitadas',
      'Integracao via API',
      'Gerente de conta dedicado',
    ],
  },
];

export function getPlanos() {
  return planos;
}
