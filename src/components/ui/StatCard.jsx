import { Icon } from './Icon';
import { tone } from './styles';

/** Indicador numerico do dashboard. */
export function StatCard({ valor, titulo, legenda, icone, tom }) {
  const t = tone(tom);
  return (
    <div className="relative flex items-center gap-4 overflow-hidden rounded-3xl border border-outline-variant bg-surface-container-lowest p-5 shadow-soft transition-shadow hover:shadow-card">
      <span className={`absolute left-0 top-0 h-full w-1 ${t.bar}`} aria-hidden="true" />
      <div className={`flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-2xl ${t.chip}`}>
        <Icon name={icone} size={24} />
      </div>
      <div>
        <p className="tabular text-3xl font-bold leading-none text-on-surface">{valor}</p>
        <p className="mt-1.5 text-sm font-semibold text-on-surface-variant">{titulo}</p>
        <p className="text-[10px] font-medium uppercase tracking-wide text-outline">{legenda}</p>
      </div>
    </div>
  );
}
