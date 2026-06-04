import { Layout } from './Layout';
import { Menu, BadgeCheck, User, Bell, Radar, Globe, HelpCircle, ChevronRight, LogOut } from 'lucide-react';
import { useNavigate } from '../Router';

export const PreferenciasPage = () => {
    const navigate = useNavigate();

    return (
        <Layout>
            <header className="flex justify-between items-center px-6 py-4 w-full z-40 bg-surface-bright/70 backdrop-blur-xl shadow-sm border-b border-outline-variant sticky top-0 md:hidden">
                 <button className="w-touch-target-min h-touch-target-min flex items-center justify-center text-primary cursor-pointer active:opacity-80 transition-colors">
                     <Menu size={24} />
                 </button>
                 <h1 className="font-headline-md text-headline-md text-primary font-bold">GeoAlerta</h1>
                 <button className="text-secondary font-bold font-body-md cursor-pointer active:opacity-80 hover:text-secondary-fixed-dim transition-colors">Profile</button>
            </header>

            <div className="max-w-container-max mx-auto px-margin-mobile md:px-margin-desktop py-8 flex flex-col gap-8 w-full flex-1">
                 <section className="bg-surface-container-lowest/80 backdrop-blur-xl rounded-xl shadow-[0_4px_20px_rgba(0,0,0,0.05)] border border-outline-variant p-6 flex items-center gap-6">
                     <div className="relative w-20 h-20 rounded-full overflow-hidden bg-surface-variant border-2 border-primary-fixed-dim">
                         <img alt="User Profile" className="object-cover w-full h-full" src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?q=80&w=150&auto=format&fit=crop"/>
                     </div>
                     <div>
                         <h2 className="font-headline-md text-headline-md text-on-surface mb-1">Roberto Oliveira</h2>
                         <p className="font-body-md text-body-md text-on-surface-variant">Gestor de Operações Agro</p>
                         <div className="mt-2 inline-flex items-center gap-1 bg-surface-container px-2 py-1 rounded-full border border-outline-variant">
                             <BadgeCheck size={14} className="text-secondary" />
                             <span className="font-label-caps text-label-caps text-on-surface-variant">Conta Enterprise</span>
                         </div>
                     </div>
                 </section>

                 <section className="flex flex-col gap-4">
                     <div className="bg-surface rounded-xl shadow-[0_2px_10px_rgba(0,0,0,0.02)] border border-outline-variant overflow-hidden">
                         <div className="px-4 py-3 bg-surface-container-low border-b border-outline-variant">
                             <span className="font-label-caps text-label-caps text-on-surface-variant uppercase">Configurações Gerais</span>
                         </div>
                         <button className="w-full flex items-center justify-between p-4 min-h-[56px] border-b border-outline-variant hover:bg-surface-container-lowest active:bg-surface-container-high transition-colors">
                             <div className="flex items-center gap-4">
                                 <div className="w-10 h-10 rounded-full bg-surface-container flex items-center justify-center text-primary">
                                     <User size={20} />
                                 </div>
                                 <div className="text-left">
                                     <p className="font-body-md text-body-md font-semibold text-on-surface">Conta</p>
                                     <p className="font-data-mono text-data-mono text-on-surface-variant text-sm">Email, Senha, CNPJ</p>
                                 </div>
                             </div>
                             <ChevronRight className="text-outline" size={20} />
                         </button>
                         <button className="w-full flex items-center justify-between p-4 min-h-[56px] border-b border-outline-variant hover:bg-surface-container-lowest active:bg-surface-container-high transition-colors">
                             <div className="flex items-center gap-4">
                                 <div className="w-10 h-10 rounded-full bg-surface-container flex items-center justify-center text-primary">
                                     <Bell size={20} />
                                 </div>
                                 <div className="text-left">
                                     <p className="font-body-md text-body-md font-semibold text-on-surface">Notificações</p>
                                     <p className="font-data-mono text-data-mono text-on-surface-variant text-sm">Alertas, Push, Email</p>
                                 </div>
                             </div>
                             <ChevronRight className="text-outline" size={20} />
                         </button>
                          <button className="w-full flex items-center justify-between p-4 min-h-[56px] hover:bg-surface-container-lowest active:bg-surface-container-high transition-colors">
                             <div className="flex items-center gap-4">
                                 <div className="w-10 h-10 rounded-full bg-surface-container flex items-center justify-center text-primary">
                                     <Radar size={20} />
                                 </div>
                                 <div className="text-left">
                                     <p className="font-body-md text-body-md font-semibold text-on-surface">Monitoramento</p>
                                     <p className="font-data-mono text-data-mono text-on-surface-variant text-sm">Sensibilidade, Limites de Risco</p>
                                 </div>
                             </div>
                             <ChevronRight className="text-outline" size={20} />
                         </button>
                     </div>

                     <div className="bg-surface rounded-xl shadow-[0_2px_10px_rgba(0,0,0,0.02)] border border-outline-variant overflow-hidden mt-4">
                         <div className="px-4 py-3 bg-surface-container-low border-b border-outline-variant">
                             <span className="font-label-caps text-label-caps text-on-surface-variant uppercase">Sistema</span>
                         </div>
                         <button className="w-full flex items-center justify-between p-4 min-h-[56px] border-b border-outline-variant hover:bg-surface-container-lowest active:bg-surface-container-high transition-colors">
                             <div className="flex items-center gap-4">
                                 <div className="w-10 h-10 rounded-full bg-surface-container flex items-center justify-center text-primary">
                                     <Globe size={20} />
                                 </div>
                                 <div className="text-left">
                                     <p className="font-body-md text-body-md font-semibold text-on-surface">Idioma e Região</p>
                                     <p className="font-data-mono text-data-mono text-on-surface-variant text-sm">Português (BR)</p>
                                 </div>
                             </div>
                             <ChevronRight className="text-outline" size={20} />
                         </button>
                         <button className="w-full flex items-center justify-between p-4 min-h-[56px] hover:bg-surface-container-lowest active:bg-surface-container-high transition-colors">
                             <div className="flex items-center gap-4">
                                 <div className="w-10 h-10 rounded-full bg-surface-container flex items-center justify-center text-primary">
                                     <HelpCircle size={20} />
                                 </div>
                                 <div className="text-left">
                                     <p className="font-body-md text-body-md font-semibold text-on-surface">Ajuda e Suporte</p>
                                     <p className="font-data-mono text-data-mono text-on-surface-variant text-sm">FAQ, Contato, Termos</p>
                                 </div>
                             </div>
                             <ChevronRight className="text-outline" size={20} />
                         </button>
                     </div>
                 </section>

                 <section className="mt-4 mb-8">
                     <button onClick={() => navigate('/')} className="w-full flex items-center justify-center gap-2 bg-error-container text-error px-4 py-4 rounded-xl border border-error/30 hover:bg-error hover:text-on-error transition-all active:scale-[0.98] min-h-touch-target-min font-body-md font-semibold">
                         <LogOut size={20} />
                         Sair da Conta
                     </button>
                 </section>
            </div>
        </Layout>
    );
};
