import { useState } from 'react';
import { Layout } from './Layout';
import { Search, AlertTriangle, Droplets, Waves, Layers, LocateFixed, Plus, Minus, X, Share2, MapPin } from 'lucide-react';
import { useNavigate } from '../Router';

export const MapaPage = () => {
    const navigate = useNavigate();
    const [selectedProperty, setSelectedProperty] = useState(true);

    return (
        <Layout>
            <div className="relative w-full h-full lg:h-[calc(100vh)] lg:-mt-[72px] flex-1">
                <div className="absolute inset-0 map-bg z-0 pointer-events-none">
                    <div className="absolute inset-0 bg-gradient-to-t from-black/40 via-transparent to-transparent z-0"></div>
                </div>

                <header className="hidden lg:flex justify-between items-center px-6 py-4 w-full z-40 bg-surface-bright/70 backdrop-blur-xl shadow-sm border-b-0 absolute top-0">
                    <h1 className="font-display-lg-mobile text-headline-md text-primary lg:invisible">GeoAlerta</h1>
                </header>

                <div className="absolute top-4 left-4 right-4 lg:left-8 lg:top-24 lg:w-96 z-20">
                    <div className="glass-panel rounded-xl shadow-sm flex items-center px-4 py-3 h-[48px]">
                        <Search className="text-on-surface-variant mr-3 w-5 h-5" />
                        <input className="bg-transparent border-none focus:ring-0 text-body-md text-on-surface w-full placeholder-on-surface-variant/70 p-0 outline-none" placeholder="Buscar fazenda, talhão ou região..." type="text"/>
                    </div>
                </div>

                <div className="absolute top-[40%] left-[30%] z-10 flex flex-col items-center">
                    <div className="relative group cursor-pointer" onClick={() => setSelectedProperty(true)}>
                        <div className="w-10 h-10 bg-error rounded-full flex items-center justify-center text-white shadow-lg pulse-marker-critical border-2 border-white">
                            <AlertTriangle size={20} />
                        </div>
                    </div>
                </div>

                <div className="absolute top-[25%] right-[20%] z-10 flex flex-col items-center">
                     <div className="relative group cursor-pointer" onClick={() => setSelectedProperty(true)}>
                        <div className="w-8 h-8 bg-[#D97706] rounded-full flex items-center justify-center text-white shadow-md pulse-marker-warning border-2 border-white">
                             <Droplets size={16} />
                        </div>
                     </div>
                </div>

                <div className="absolute bottom-[35%] right-[35%] z-10 flex flex-col items-center">
                     <div className="relative group cursor-pointer" onClick={() => setSelectedProperty(true)}>
                        <div className="w-8 h-8 bg-[#EA580C] rounded-full flex items-center justify-center text-white shadow-md border-2 border-white">
                            <Waves size={16} />
                        </div>
                    </div>
                </div>

                 <div className="absolute right-4 bottom-24 lg:bottom-12 flex flex-col gap-3 z-20">
                    <button className="w-[44px] h-[44px] glass-panel rounded-full flex items-center justify-center text-on-surface hover:bg-surface-container transition-colors shadow-sm active:scale-95">
                        <Layers size={20} />
                    </button>
                    <button className="w-[44px] h-[44px] glass-panel rounded-full flex items-center justify-center text-on-surface hover:bg-surface-container transition-colors shadow-sm active:scale-95">
                        <LocateFixed size={20} />
                    </button>
                    <div className="glass-panel rounded-[22px] flex flex-col overflow-hidden shadow-sm mt-2">
                        <button className="w-[44px] h-[44px] flex items-center justify-center text-on-surface hover:bg-surface-container transition-colors active:bg-surface-variant border-b border-outline-variant/30">
                            <Plus size={20} />
                        </button>
                        <button className="w-[44px] h-[44px] flex items-center justify-center text-on-surface hover:bg-surface-container transition-colors active:bg-surface-variant">
                            <Minus size={20} />
                        </button>
                    </div>
                </div>

                <div className={`absolute bottom-16 lg:bottom-4 left-0 w-full lg:left-8 lg:w-96 p-4 z-30 transition-transform duration-300 ${selectedProperty ? 'translate-y-0' : 'translate-y-[120%]'}`}>
                     <div className="glass-panel rounded-xl shadow-[0_-4px_20px_rgba(0,0,0,0.05)] border-t-[4px] border-error overflow-hidden">
                         <div className="w-12 h-1 bg-outline-variant rounded-full mx-auto mt-3 mb-2 lg:hidden"></div>
                         <div className="p-5">
                             <div className="flex justify-between items-start mb-4">
                                <div>
                                    <h2 className="font-headline-md text-headline-md text-on-surface mb-1">Fazenda Otor</h2>
                                    <p className="font-body-md text-on-surface-variant flex items-center gap-1">
                                        <MapPin size={16} /> Vale do Rio Verde, MG
                                    </p>
                                </div>
                                <button onClick={() => setSelectedProperty(false)} className="text-on-surface-variant hover:text-on-surface rounded-full p-1 bg-surface-container-highest/50">
                                    <X size={20} />
                                </button>
                             </div>
                             <div className="grid grid-cols-2 gap-4 mb-5">
                                 <div className="bg-white/50 rounded-lg p-3">
                                     <p className="font-label-caps text-on-surface-variant mb-1">ÁREA TOTAL</p>
                                     <p className="font-data-mono text-on-surface font-semibold">1.250 ha</p>
                                 </div>
                                  <div className="bg-white/50 rounded-lg p-3">
                                     <p className="font-label-caps text-on-surface-variant mb-1">CULTURA ATUAL</p>
                                     <p className="font-data-mono text-on-surface font-semibold">Soja (V6)</p>
                                 </div>
                             </div>
                             <div className="space-y-3">
                                 <h3 className="font-label-caps text-on-surface-variant">RISCOS ATIVOS</h3>
                                 <div className="flex items-center gap-3 p-3 rounded-lg bg-error-container/40 border border-error/20">
                                     <div className="w-2 h-full bg-error rounded-full self-stretch"></div>
                                     <div className="flex-grow">
                                         <p className="font-body-md font-semibold text-on-surface">Stress Hídrico Severo</p>
                                         <p className="font-data-mono text-on-surface-variant text-sm">Zona A (320 ha afetados)</p>
                                     </div>
                                     <span className="px-2 py-1 bg-error text-white font-label-caps rounded-full text-[10px]">CRÍTICO</span>
                                 </div>
                                 <div className="flex items-center gap-3 p-3 rounded-lg bg-[#FEF3C7]/60 border border-[#F59E0B]/20">
                                     <div className="w-2 h-full bg-[#D97706] rounded-full self-stretch"></div>
                                     <div className="flex-grow">
                                         <p className="font-body-md font-semibold text-on-surface">Anomalia de Biomassa</p>
                                         <p className="font-data-mono text-on-surface-variant text-sm">Zona C (Queda de 15% NDVI)</p>
                                     </div>
                                     <span className="px-2 py-1 bg-[#D97706] text-white font-label-caps rounded-full text-[10px]">ATENÇÃO</span>
                                 </div>
                             </div>
                             <div className="mt-5 pt-4 border-t border-outline-variant/30 flex gap-3">
                                 <button className="flex-1 bg-primary-container text-white font-body-md font-medium py-3 rounded-lg hover:bg-primary-container/90 transition-colors">
                                     Ver Detalhes
                                 </button>
                                 <button className="w-[48px] border border-outline rounded-lg flex items-center justify-center text-on-surface hover:bg-surface-container transition-colors">
                                     <Share2 size={20} />
                                 </button>
                             </div>
                         </div>
                     </div>
                </div>

                <div className="absolute bottom-20 lg:bottom-6 right-4 lg:right-8 z-10 hidden sm:block">
                    <div className="glass-panel px-3 py-2 rounded-lg shadow-sm">
                        <p className="font-data-mono text-[11px] text-on-surface-variant">
                             <span className="font-semibold text-on-surface">Real-time Satellite Data</span> | Last Update: 14:32:05<br/>
                             Resolution: 10m | Sensor: Sentinel-2B
                        </p>
                    </div>
                </div>
            </div>
        </Layout>
    );
};
