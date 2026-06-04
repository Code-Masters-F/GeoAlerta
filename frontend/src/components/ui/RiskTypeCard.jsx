import { Icon } from './Icon';
import { tone } from './styles';

/** Cartao de tipo de risco na landing. */
export function RiskTypeCard({ risco }) {
  const t = tone(risco.tom);
  return (
    <div className="rounded-2xl border border-outline-variant bg-surface-container-low p-6 transition-all hover:-translate-y-1 hover:shadow-card">
      <div className="mb-4 flex items-start justify-between">
        <div className={`flex h-12 w-12 items-center justify-center rounded-xl ${t.chip}`}>
          <Icon name={risco.icone} size={24} />
        </div>
        <span className="rounded-full bg-surface-container px-2.5 py-1 text-[10px] font-bold uppercase tracking-wide text-on-surface-variant">
          {risco.nivel}
        </span>
      </div>
      <h3 className="mb-2 text-lg font-bold text-on-surface">{risco.nome}</h3>
      <p className="text-sm leading-relaxed text-on-surface-variant">{risco.descricao}</p>
    </div>
  );
}
