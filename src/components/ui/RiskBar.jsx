import { severity } from './styles';

/** Barra de probabilidade de risco no dashboard. */
export function RiskBar({ nome, valor, severidade }) {
  const s = severity(severidade);
  return (
    <div>
      <div className="mb-2 flex justify-between text-sm">
        <span className="font-semibold text-on-surface">{nome}</span>
        <span className={`tabular font-bold ${s.text}`}>{valor}%</span>
      </div>
      <div
        className="h-2 w-full overflow-hidden rounded-full bg-surface-container-high"
        role="progressbar"
        aria-valuenow={valor}
        aria-valuemin={0}
        aria-valuemax={100}
        aria-label={`Probabilidade de ${nome}`}
      >
        <div className={`h-full rounded-full ${s.solid}`} style={{ width: `${valor}%` }} />
      </div>
    </div>
  );
}
