import { Icon } from './Icon';
import { severity } from './styles';

/** Linha de alerta recente no dashboard. */
export function AlertItem({ titulo, local, prioridade, icone, severidade }) {
  const s = severity(severidade);
  return (
    <div className={`flex items-start gap-4 rounded-2xl border p-3 transition-colors ${s.card}`}>
      <div className={`flex h-10 w-10 flex-shrink-0 items-center justify-center rounded-xl text-white ${s.solid}`}>
        <Icon name={icone} size={20} />
      </div>
      <div className="min-w-0 flex-1">
        <p className="truncate text-sm font-bold text-on-surface">{titulo}</p>
        <p className="truncate text-xs text-on-surface-variant">
          {local} • {prioridade}
        </p>
      </div>
      <span className={`flex-shrink-0 rounded-full border px-2 py-1 text-[10px] font-bold uppercase ${s.badge}`}>
        {s.label}
      </span>
    </div>
  );
}
