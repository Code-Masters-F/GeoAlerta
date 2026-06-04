import { Home, Compass, AlertTriangle, Sprout, MoreHorizontal } from 'lucide-react';
import { useNavigate, useCurrentRoute } from '../Router';

export const BottomBar = () => {
    const navigate = useNavigate();
    const currentRoute = useCurrentRoute();

    const items = [
        { icon: Home, label: 'Início', path: '/dashboard' },
        { icon: Compass, label: 'Mapa', path: '/mapa' },
        { icon: AlertTriangle, label: 'Alertas', path: '#' },
        { icon: Sprout, label: 'Fazendas', path: '/propriedades' },
        { icon: MoreHorizontal, label: 'Mais', path: '/preferencias' }
    ];

    return (
        <nav className="fixed bottom-0 left-0 w-full z-50 flex justify-around items-center h-16 px-4 pb-safe lg:hidden bg-white/80 backdrop-blur-md shadow-[0_-4px_20px_rgba(0,0,0,0.05)] border-t border-outline-variant rounded-t-xl">
            {items.map(item => {
                const isActive = item.path === '/preferencias' ? currentRoute === '/preferencias' : (currentRoute === item.path);
                
                return (
                    <a
                        key={item.label}
                        onClick={() => item.path !== '#' && navigate(item.path)}
                        className={`flex flex-col items-center justify-center cursor-pointer active:scale-90 transition-transform w-[44px] h-[44px] relative ${isActive ? 'text-secondary font-bold' : 'text-on-surface-variant hover:text-secondary'}`}
                    >
                        {item.label === 'Alertas' && (
                           <span className="absolute top-0 right-1 w-2 h-2 bg-error rounded-full"></span>
                        )}
                        <item.icon size={20} className="mb-1" />
                        <span className="font-label-caps text-[10px]">{item.label}</span>
                        {isActive && item.label === 'Fazendas' && <div className="absolute -top-3 w-8 h-1 bg-secondary rounded-b-md"></div>}
                    </a>
                )
            })}
        </nav>
    );
};
