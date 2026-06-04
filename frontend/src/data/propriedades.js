/**
 * Propriedades monitoradas.
 * Mock hoje; trocar `getPropriedades` por `fetch('/api/propriedades')` depois.
 */

const propriedades = [
  {
    id: 'palmares',
    nome: 'Fazenda Palmares',
    regiao: 'Cerrado, GO',
    cultura: 'Soja',
    culturaIcone: 'sprout',
    area: '1.200 ha',
    severidade: 'critico',
    imagem:
      'https://images.unsplash.com/photo-1524661135-423995f22d0b?q=80&w=1000&auto=format&fit=crop',
  },
  {
    id: 'valeverde',
    nome: 'Vale Verde',
    regiao: 'Sorriso, MT',
    cultura: 'Milho',
    culturaIcone: 'tractor',
    area: '3.500 ha',
    severidade: 'normal',
    imagem:
      'https://images.unsplash.com/photo-1581068222533-eb0ef59503ea?q=80&w=1000&auto=format&fit=crop',
  },
  {
    id: 'alvorada',
    nome: 'Sitio Alvorada',
    regiao: 'Cascavel, PR',
    cultura: 'Trigo',
    culturaIcone: 'leaf',
    area: '850 ha',
    severidade: 'atencao',
    imagem:
      'https://images.unsplash.com/photo-1488190211105-8b0e65b80b4e?q=80&w=1000&auto=format&fit=crop',
  },
];

export function getPropriedades() {
  return propriedades;
}
