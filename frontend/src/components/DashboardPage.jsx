import { Bell, Menu, Search, Globe2 } from 'lucide-react';
import { Layout } from './Layout';
import { useNavigate } from '../Router';
import { StatCard } from './ui/StatCard';
import { AlertItem } from './ui/AlertItem';
import { RiskBar } from './ui/RiskBar';
import { getIndicadores, getAlertasRecentes, getProbabilidadesDeRisco } from '../data/dashboard';

export const DashboardPage = () => {
  const navigate = useNavigate();
  const indicadores = getIndicadores();
  const alertas = getAlertasRecentes();
  const probabilidades = getProbabilidadesDeRisco();

  return (
    <Layout>
      <header className="sticky top-0 z-40 flex w-full items-center justify-between border-b border-outline-variant bg-surface-bright/90 px-6 py-4 backdrop-blur-md">
        <div className="flex items-center gap-4">
          <button
            className="rounded-full p-2 text-on-surface-variant transition-colors hover:bg-surface-container lg:hidden"
            aria-label="Abrir menu"
          >
            <Menu />
          </button>
          <div className="flex items-center gap-2 text-sm">
            <span className="hidden font-medium text-on-surface-variant md:block">Dashboard</span>
            <span className="hidden text-outline-variant md:block">/</span>
            <span className="font-semibold text-on-surface">Visao Geral</span>
          </div>
        </div>
        <div className="flex items-center gap-3">
          <div className="relative hidden md:block">
            <Search className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-on-surface-variant" aria-hidden="true" />
            <input
              className="w-64 rounded-full border border-outline-variant bg-surface-container-low py-2 pl-10 pr-4 text-sm transition-all focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/15"
              placeholder="Buscar propriedade..."
              type="text"
              aria-label="Buscar propriedade"
            />
          </div>
          <button className="relative rounded-full p-2 text-on-surface-variant transition-colors hover:bg-surface-container" aria-label="Notificacoes">
            <Bell />
            <span className="absolute right-1 top-1 h-2.5 w-2.5 rounded-full border-2 border-surface-bright bg-error" />
          </button>
          <div className="hidden items-center gap-3 border-l border-outline-variant pl-4 md:flex">
            <div className="text-right">
              <p className="text-xs font-bold text-on-surface">Fazenda Palmares</p>
              <p className="text-[10px] text-on-surface-variant">Admin Regional</p>
            </div>
            <img
              alt="Foto do perfil"
              className="h-10 w-10 rounded-full border-2 border-surface-bright object-cover shadow-sm"
              src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?q=80&w=150&auto=format&fit=crop"
            />
          </div>
        </div>
      </header>

      <div className="mx-auto w-full max-w-7xl flex-1 space-y-6 p-4 pb-24 md:p-6 lg:pb-12">
        <section className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-4" aria-label="Indicadores">
          {indicadores.map((indicador) => (
            <StatCard key={indicador.id} {...indicador} />
          ))}
        </section>

        <div className="grid grid-cols-1 gap-6 lg:grid-cols-3">
          <section className="flex flex-col overflow-hidden rounded-3xl border border-outline-variant bg-surface-container-lowest shadow-soft lg:col-span-2">
            <div className="flex items-center justify-between p-6 pb-4">
              <h2 className="text-sm font-bold uppercase tracking-wider text-on-surface">Alertas Recentes</h2>
              <button className="text-sm font-semibold text-primary transition-colors hover:text-primary-container">Ver todos</button>
            </div>
            <div className="flex-1 space-y-3 px-4 pb-5">
              {alertas.map((alerta) => (
                <AlertItem key={alerta.id} {...alerta} />
              ))}
            </div>
          </section>

          <section className="flex flex-col rounded-3xl border border-outline-variant bg-surface-container-lowest p-6 shadow-soft">
            <div className="mb-6">
              <h2 className="text-sm font-bold uppercase tracking-wider text-on-surface">Probabilidade de Riscos</h2>
              <p className="mt-1 text-xs text-on-surface-variant">Media entre propriedades hoje</p>
            </div>
            <div className="flex-1 space-y-6">
              {probabilidades.map((risco) => (
                <RiskBar key={risco.id} {...risco} />
              ))}
            </div>
          </section>
        </div>

        <section className="group relative min-h-[300px] overflow-hidden rounded-3xl border border-outline-variant bg-surface-container">
          <div
            className="absolute inset-0 bg-surface-container-high"
            style={{ backgroundImage: 'radial-gradient(var(--color-outline) 1.5px, transparent 1.5px)', backgroundSize: '24px 24px', opacity: 0.4 }}
            aria-hidden="true"
          />
          <div className="absolute left-1/3 top-1/4 h-16 w-20 rounded-lg border-2 border-primary bg-primary/20" aria-hidden="true" />
          <div className="absolute bottom-1/3 right-1/4 h-24 w-32 rounded-lg border-2 border-error bg-error/15" aria-hidden="true" />
          <div className="absolute inset-0 flex items-center justify-center">
            <button
              onClick={() => navigate('/mapa')}
              className="inline-flex items-center gap-2 rounded-2xl bg-agro-dark px-8 py-4 font-bold text-white shadow-lift transition-transform hover:scale-105 focus-visible:outline-none focus-visible:ring-4 focus-visible:ring-primary/30"
            >
              <Globe2 size={20} aria-hidden="true" /> Abrir mapa interativo
            </button>
          </div>
          <div className="tabular absolute left-4 top-4 rounded-lg bg-surface-bright/90 px-3 py-1.5 text-xs font-bold text-on-surface shadow-sm backdrop-blur">
            COORD: 22 14 15 S 54 48 21 W
          </div>
        </section>
      </div>
    </Layout>
  );
};
