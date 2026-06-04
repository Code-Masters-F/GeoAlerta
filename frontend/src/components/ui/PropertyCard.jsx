import { ChevronRight, MapPin } from 'lucide-react';
import { Icon } from './Icon';
import { severity } from './styles';

/** Cartao de propriedade na listagem. */
export function PropertyCard({ propriedade, onClick }) {
  const { nome, regiao, cultura, culturaIcone, area, severidade } = propriedade;
  const s = severity(severidade);
  return (
    <article
      onClick={onClick}
      className={`group relative cursor-pointer overflow-hidden rounded-2xl border-t-4 bg-surface-container-lowest p-4 shadow-soft transition-all hover:-translate-y-0.5 hover:shadow-card active:scale-[0.99] ${s.topBorder}`}
    >
      <div className="flex items-start gap-4">
        <div className="relative h-20 w-20 flex-shrink-0 overflow-hidden rounded-xl bg-surface-container-high">
          <img alt={`Vista da ${nome}`} className="h-full w-full object-cover" src={propriedade.imagem} loading="lazy" />
        </div>
        <div className="flex min-w-0 flex-1 flex-col gap-1">
          <div className="flex w-full items-start justify-between">
            <h2 className="truncate font-headline-md text-lg font-bold text-on-surface">{nome}</h2>
            <ChevronRight className="text-outline transition-colors group-hover:text-primary" size={20} />
          </div>
          <p className="flex items-center gap-1 text-sm text-on-surface-variant">
            <MapPin size={16} aria-hidden="true" /> {regiao}
          </p>
          <div className="mt-2 flex flex-wrap gap-2">
            <span className="flex items-center gap-1 rounded-full bg-surface-container px-2.5 py-1 text-xs font-semibold text-on-surface">
              <Icon name={culturaIcone} size={14} /> {cultura}
            </span>
            <span className="tabular rounded-full bg-surface-container px-2.5 py-1 text-xs font-semibold text-on-surface">
              {area}
            </span>
          </div>
        </div>
      </div>
      <div className={`absolute bottom-4 right-4 rounded-full border px-3 py-1 text-[10px] font-bold uppercase shadow-sm backdrop-blur ${s.badge}`}>
        {s.label}
      </div>
    </article>
  );
}
