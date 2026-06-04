import { LayoutDashboard, Map, Bell, Tractor, BarChart3, Settings, Leaf } from 'lucide-react';
import { useNavigate, useCurrentRoute } from '../Router';

const menuItems = [
  { icon: LayoutDashboard, label: 'Visao Geral', path: '/dashboard' },
  { icon: Map, label: 'Mapa Inteligente', path: '/mapa' },
  { icon: Bell, label: 'Alertas', path: '#' },
  { icon: Tractor, label: 'Propriedades', path: '/propriedades' },
  { icon: BarChart3, label: 'Relatorios', path: '#' },
  { icon: Settings, label: 'Preferencias', path: '/preferencias' },
];

export const Sidebar = () => {
  const navigate = useNavigate();
  const currentRoute = useCurrentRoute();

  return (
    <aside className="fixed left-0 top-0 z-50 hidden h-screen w-64 flex-col border-r border-white/10 bg-agro-dark p-4 lg:flex">
      <div className="mb-8 flex items-center gap-3 px-4">
        <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-primary text-white">
          <Leaf size={18} aria-hidden="true" />
        </div>
        <span className="font-display text-xl font-bold tracking-tight text-white">GeoAlerta</span>
      </div>

      <nav className="flex-1 space-y-1">
        {menuItems.map((item) => {
          const isActive = currentRoute === item.path;
          const disabled = item.path === '#';
          return (
            <button
              key={item.label}
              onClick={() => !disabled && navigate(item.path)}
              aria-current={isActive ? 'page' : undefined}
              className={`flex w-full items-center gap-3 rounded-xl px-4 py-3 text-sm font-medium transition-colors ${
                isActive ? 'bg-white/15 text-white' : 'text-white/70 hover:bg-white/5 hover:text-white'
              } ${disabled ? 'cursor-default opacity-50' : ''}`}
            >
              <item.icon size={20} aria-hidden="true" />
              <span>{item.label}</span>
            </button>
          );
        })}
      </nav>

      <div className="mt-auto border-t border-white/10 pt-6">
        <div className="rounded-xl bg-white/5 p-4">
          <p className="text-xs font-bold uppercase tracking-wider text-cta">Assinatura Pro</p>
          <p className="mt-1 text-sm text-white/80">Renovacao em 12 dias</p>
        </div>
      </div>
    </aside>
  );
};
