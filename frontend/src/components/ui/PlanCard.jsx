import { Check } from 'lucide-react';
import { Button } from './Button';

/** Cartao de plano comercial na landing. */
export function PlanCard({ plano, onSelect }) {
  const { nome, descricao, preco, periodo, beneficios, destaque, cta } = plano;

  if (destaque) {
    return (
      <div className="relative rounded-3xl border border-primary-container bg-agro-dark p-8 text-white shadow-lift md:-translate-y-4">
        <span className="absolute left-1/2 top-0 -translate-x-1/2 -translate-y-1/2 rounded-full bg-cta px-3 py-1 text-xs font-bold uppercase tracking-wider text-on-cta">
          Mais popular
        </span>
        <h3 className="text-xl font-bold">{nome}</h3>
        <p className="mb-6 mt-2 h-10 text-sm text-white/70">{descricao}</p>
        <Preco preco={preco} periodo={periodo} tom="claro" />
        <Beneficios itens={beneficios} tom="claro" />
        <Button variant="cta" className="w-full" onClick={onSelect}>
          {cta}
        </Button>
      </div>
    );
  }

  return (
    <div className="rounded-3xl border border-outline-variant bg-surface-container-lowest p-8 shadow-soft transition-all hover:-translate-y-1 hover:shadow-card">
      <h3 className="text-xl font-bold text-on-surface">{nome}</h3>
      <p className="mb-6 mt-2 h-10 text-sm text-on-surface-variant">{descricao}</p>
      <Preco preco={preco} periodo={periodo} />
      <Beneficios itens={beneficios} />
      <Button variant="outline" className="w-full" onClick={onSelect}>
        {cta}
      </Button>
    </div>
  );
}

function Preco({ preco, periodo, tom }) {
  const cor = tom === 'claro' ? 'text-white' : 'text-on-surface';
  const corPeriodo = tom === 'claro' ? 'text-white/60' : 'text-on-surface-variant';
  return (
    <div className="mb-6">
      <span className={`tabular text-4xl font-bold ${cor}`}>{preco}</span>
      {periodo ? <span className={`text-sm ${corPeriodo}`}>{periodo}</span> : null}
    </div>
  );
}

function Beneficios({ itens, tom }) {
  const cor = tom === 'claro' ? 'text-white/85' : 'text-on-surface-variant';
  return (
    <ul className={`mb-8 space-y-3 text-sm ${cor}`}>
      {itens.map((item) => (
        <li key={item} className="flex items-center gap-2">
          <Check className="flex-shrink-0 text-success" size={16} aria-hidden="true" /> {item}
        </li>
      ))}
    </ul>
  );
}
