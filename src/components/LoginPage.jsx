import { useState } from 'react';
import { Building2, Lock, EyeOff, LogIn, Eye, ShieldCheck } from 'lucide-react';
import { useNavigate } from '../Router';

export const LoginPage = () => {
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);

    return (
        <main className="grid grid-cols-1 md:grid-cols-2 min-h-screen w-full bg-surface-container-lowest font-body-md text-on-surface antialiased">
            <section className="hidden md:flex relative flex-col justify-between p-margin-desktop bg-cover bg-center bg-no-repeat overflow-hidden" style={{backgroundImage: "url('https://images.unsplash.com/photo-1524661135-423995f22d0b?q=80&w=1000&auto=format&fit=crop')"}}>
                <div className="absolute inset-0 bg-gradient-to-b from-primary/50 via-primary/20 to-primary/90 pointer-events-none"></div>
                <header className="relative z-10 flex items-center gap-3 cursor-pointer" onClick={() => navigate('/')}>
                    <div className="bg-surface-container-lowest p-2 rounded-lg shadow-lg">
                        <Building2 className="text-primary-container w-[28px] h-[28px]" />
                    </div>
                    <span className="font-headline-md text-headline-md font-bold text-white tracking-tight">GeoAlerta</span>
                </header>
                <div className="relative z-10 max-w-lg mb-12">
                    <h1 className="font-display-lg text-display-lg text-white mb-6 drop-shadow-md">Proteja sua lavoura antes que o risco chegue.</h1>
                    <p className="font-body-lg text-body-lg text-white/90 mb-12 drop-shadow">Monitoramento em tempo real de riscos climáticos, biológicos e ambientais para o agronegócio brasileiro.</p>
                </div>
            </section>
            
            <section className="flex flex-col justify-center items-center p-margin-mobile md:p-margin-desktop bg-surface-container-lowest relative min-h-screen">
                <header className="md:hidden flex items-center gap-3 w-full max-w-[420px] mb-12 cursor-pointer" onClick={() => navigate('/')}>
                     <div className="bg-primary-container p-2 rounded-lg">
                        <Building2 className="text-secondary-container w-6 h-6" />
                     </div>
                     <span className="font-headline-md text-headline-md font-bold text-primary-container tracking-tight">GeoAlerta</span>
                </header>
                
                <div className="w-full max-w-[420px]">
                    <div className="mb-10">
                        <h2 className="font-display-lg-mobile text-display-lg-mobile text-on-surface mb-2">Entrar na sua conta</h2>
                        <p className="text-on-surface-variant font-body-md text-body-md">Acesse o painel da sua empresa agrícola</p>
                    </div>

                    <form className="flex flex-col gap-6" onSubmit={(e) => { e.preventDefault(); navigate('/dashboard'); }}>
                        <div className="flex flex-col gap-2">
                            <label className="font-label-caps text-label-caps text-on-surface-variant" htmlFor="cnpj">CNPJ da empresa</label>
                            <div className="relative group">
                                <Building2 className="absolute left-4 top-1/2 -translate-y-1/2 text-outline group-focus-within:text-secondary transition-colors" size={20} />
                                <input className="w-full h-touch-target-min pl-12 pr-4 bg-surface rounded-lg border border-outline-variant text-on-surface font-data-mono text-data-mono placeholder:text-outline-variant focus:outline-none focus:ring-2 focus:ring-secondary/20 focus:border-secondary transition-all shadow-sm" id="cnpj" placeholder="00.000.000/0001-00" type="text"/>
                            </div>
                        </div>

                        <div className="flex flex-col gap-2">
                             <div className="flex justify-between items-end">
                                <label className="font-label-caps text-label-caps text-on-surface-variant" htmlFor="password">Senha</label>
                                <a className="font-label-caps text-label-caps text-secondary hover:text-secondary-fixed-dim transition-colors" href="#">Esqueci minha senha</a>
                            </div>
                             <div className="relative group">
                                <Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-outline group-focus-within:text-secondary transition-colors" size={20} />
                                <input className="w-full h-touch-target-min pl-12 pr-12 bg-surface rounded-lg border border-outline-variant text-on-surface font-data-mono text-data-mono placeholder:text-outline-variant focus:outline-none focus:ring-2 focus:ring-secondary/20 focus:border-secondary transition-all shadow-sm" id="password" placeholder="••••••••" type={showPassword ? 'text' : 'password'}/>
                                <button type="button" onClick={() => setShowPassword(!showPassword)} className="absolute right-4 top-1/2 -translate-y-1/2 text-outline hover:text-on-surface transition-colors focus:outline-none">
                                    {showPassword ? <Eye size={20} /> : <EyeOff size={20} />}
                                </button>
                            </div>
                        </div>

                         <div className="flex items-center gap-3 mt-2">
                            <input className="w-5 h-5 rounded border-outline-variant text-secondary focus:ring-secondary/20 bg-surface cursor-pointer" id="remember" type="checkbox"/>
                            <label className="font-body-md text-body-md text-on-surface-variant cursor-pointer select-none" htmlFor="remember">Manter conectado por 30 dias</label>
                        </div>

                        <button type="submit" className="w-full h-touch-target-min mt-4 bg-primary-container text-white rounded-lg hover:bg-primary transition-all flex items-center justify-center gap-2 font-bold shadow-[0_4px_14px_rgba(11,36,20,0.15)] hover:shadow-[0_6px_20px_rgba(11,36,20,0.2)] hover:-translate-y-0.5">
                            <LogIn size={20} /> Entrar
                        </button>
                    </form>

                     <div className="flex items-center gap-4 my-8 opacity-60">
                        <div className="flex-1 h-px bg-outline-variant"></div>
                        <span className="font-label-caps text-label-caps text-outline">ou</span>
                        <div className="flex-1 h-px bg-outline-variant"></div>
                    </div>

                    <div className="bg-surface-variant/40 rounded-xl p-6 border border-outline-variant/50 flex flex-col sm:flex-row items-center justify-between gap-4 transition-colors hover:bg-surface-variant/60">
                        <div className="text-center sm:text-left">
                            <h3 className="font-bold text-on-surface mb-1">Ainda não tem conta?</h3>
                            <p className="font-body-md text-body-md text-on-surface-variant text-[14px]">Cadastre sua empresa e comece em minutos</p>
                        </div>
                        <button onClick={() => navigate('/cadastro')} className="whitespace-nowrap px-6 h-touch-target-min border-2 border-primary-container text-primary-container rounded-lg font-bold hover:bg-primary-container hover:text-white transition-all w-full sm:w-auto">
                            Cadastrar empresa
                        </button>
                    </div>
                     <div className="mt-8 flex items-center justify-center gap-2 text-[13px] text-on-surface-variant font-medium">
                        <ShieldCheck size={18} className="text-secondary" />
                        14 dias grátis no plano Profissional (sem cartão de crédito)
                    </div>
                </div>
            </section>
        </main>
    );
};
