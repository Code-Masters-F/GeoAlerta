import { Search, Plus, Menu } from 'lucide-react';
import { Layout } from './Layout';
import { Button } from './ui/Button';
import { PropertyCard } from './ui/PropertyCard';
import { getPropriedades } from '../data/propriedades';

export const PropriedadesPage = () => {
  const propriedades = getPropriedades();

  return (
    <Layout>
      <header className="sticky top-0 z-40 flex w-full items-center justify-between border-b border-outline-variant bg-surface-bright/80 px-6 py-4 shadow-sm backdrop-blur-xl md:hidden">
        <button className="flex h-11 w-11 items-center justify-center text-primary transition-colors active:opacity-80" aria-label="Abrir menu">
          <Menu size={24} />
        </button>
        <h1 className="font-headline-md text-xl font-bold text-primary">GeoAlerta</h1>
        <div className="h-9 w-9 overflow-hidden rounded-full bg-surface-container-high">
          <img alt="Foto do perfil" src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?q=80&w=150&auto=format&fit=crop" />
        </div>
      </header>

      <div className="mx-auto flex w-full max-w-7xl flex-1 flex-col gap-6 px-4 pb-32 pt-6 lg:p-12">
        <div>
          <h1 className="font-headline-md text-2xl font-bold text-on-surface">Suas Propriedades</h1>
          <p className="mt-1 text-sm text-on-surface-variant">
            {propriedades.length} propriedades monitoradas em tempo real
          </p>
        </div>

        <section className="flex flex-col gap-4">
          <div className="relative w-full">
            <Search className="absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-outline" aria-hidden="true" />
            <input
              className="h-12 w-full rounded-xl border border-outline-variant bg-surface-container-lowest pl-12 pr-4 text-on-surface shadow-sm transition-all focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/15"
              placeholder="Buscar propriedades..."
              type="text"
              aria-label="Buscar propriedades"
            />
          </div>
          <Button variant="primary" className="w-full">
            <Plus size={20} aria-hidden="true" /> Adicionar Propriedade
          </Button>
        </section>

        <section className="flex flex-col gap-4" aria-label="Lista de propriedades">
          {propriedades.map((propriedade) => (
            <PropertyCard key={propriedade.id} propriedade={propriedade} />
          ))}
        </section>
      </div>
    </Layout>
  );
};
