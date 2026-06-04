import { useState } from 'react';
import { ArrowLeft, Building2, CreditCard, Mail, Lock, KeyRound, EyeOff, Eye, ArrowRight } from 'lucide-react';
import { useNavigate } from '../Router';

export const CadastroPage = () => {
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);

    return (
        <div className="bg-background text-on-background min-h-screen flex flex-col font-body-md relative overflow-x-hidden">
            <div className="fixed inset-0 z-0 pointer-events-none overflow-hidden">
                <div className="absolute -top-[10%] -right-[10%] w-[50%] h-[50%] rounded-full bg-surface-container-high/40 blur-[100px]"></div>
                <div className="absolute -bottom-[10%] -left-[10%] w-[60%] h-[60%] rounded-full bg-primary-fixed/20 blur-[120px]"></div>
            </div>

            <header className="w-full top-0 bg-surface/70 backdrop-blur-md border-b border-outline-variant shadow-sm z-50 sticky">
                <div className="flex justify-between items-center w-full px-margin-mobile md:px-margin-desktop py-4 max-w-container-max mx-auto">
                    <button onClick={() => navigate('/')} className="w-touch-target-min h-touch-target-min flex items-center justify-center rounded-full hover:bg-surface-variant transition-colors group">
                        <ArrowLeft className="text-primary dark:text-primary-fixed group-hover:text-secondary-container transition-colors duration-200" size={24} />
                    </button>
                    <div className="font-display-lg text-primary dark:text-primary-fixed tracking-tight text-xl md:text-2xl font-bold cursor-pointer" onClick={() => navigate('/')}>GeoAlerta</div>
                    <button onClick={() => navigate('/login')} className="text-on-surface-variant dark:text-on-surface-variant hover:text-secondary-container transition-colors duration-200 font-label-caps text-label-caps px-2 py-2 flex items-center justify-center min-h-[44px]">Login</button>
                </div>
            </header>

            <main className="flex-grow flex flex-col justify-center items-center px-margin-mobile py-8 md:py-12 z-10 w-full relative">
                 <div className="w-full max-w-md glass-panel rounded-xl shadow-tinted p-6 md:p-8">
                     <div className="text-center mb-8">
                        <h1 className="font-display-lg-mobile text-display-lg-mobile text-primary-container mb-3">Cadastre sua empresa</h1>
                        <p className="font-body-md text-body-md text-on-surface-variant">Inicie o monitoramento inteligente de suas propriedades agora mesmo.</p>
                    </div>

                    <form className="space-y-5" onSubmit={(e) => { e.preventDefault(); navigate('/dashboard'); }}>
                        <div className="space-y-1">
                            <label className="font-label-caps text-label-caps text-on-surface block ml-1" htmlFor="companyName">Nome da Empresa</label>
                            <div className="relative">
                                <Building2 className="absolute left-3 top-1/2 -translate-y-1/2 text-outline-variant" size={20} />
                                <input className="w-full h-12 pl-10 pr-4 rounded-lg bg-surface-container-lowest border border-outline-variant text-on-surface placeholder:text-outline-variant input-focus-glow transition-all" id="companyName" placeholder="GeoAlerta S/A" required type="text"/>
                            </div>
                        </div>

                         <div className="space-y-1">
                            <label className="font-label-caps text-label-caps text-on-surface block ml-1" htmlFor="cnpj">CNPJ da Empresa</label>
                            <div className="relative">
                                <CreditCard className="absolute left-3 top-1/2 -translate-y-1/2 text-outline-variant" size={20} />
                                <input className="w-full h-12 pl-10 pr-4 rounded-lg bg-surface-container-lowest border border-outline-variant text-on-surface placeholder:text-outline-variant input-focus-glow transition-all" id="cnpj" placeholder="00.000.000/0000-00" required type="text"/>
                            </div>
                        </div>

                        <div className="space-y-1">
                            <label className="font-label-caps text-label-caps text-on-surface block ml-1" htmlFor="email">E-mail Corporativo</label>
                            <div className="relative">
                                <Mail className="absolute left-3 top-1/2 -translate-y-1/2 text-outline-variant" size={20} />
                                <input className="w-full h-12 pl-10 pr-4 rounded-lg bg-surface-container-lowest border border-outline-variant text-on-surface placeholder:text-outline-variant input-focus-glow transition-all" id="email" placeholder="contato@empresa.com" required type="email"/>
                            </div>
                        </div>

                        <div className="space-y-1">
                            <label className="font-label-caps text-label-caps text-on-surface block ml-1" htmlFor="password">Senha</label>
                            <div className="relative">
                                <Lock className="absolute left-3 top-1/2 -translate-y-1/2 text-outline-variant" size={20} />
                                <input className="w-full h-12 pl-10 pr-10 rounded-lg bg-surface-container-lowest border border-outline-variant text-on-surface placeholder:text-outline-variant input-focus-glow transition-all" id="password" placeholder="••••••••" required type={showPassword ? 'text' : 'password'}/>
                                <button type="button" onClick={() => setShowPassword(!showPassword)} className="absolute right-3 top-1/2 -translate-y-1/2 text-outline-variant hover:text-primary-container transition-colors w-8 h-8 flex items-center justify-center">
                                    {showPassword ? <Eye size={18} /> : <EyeOff size={18} />}
                                </button>
                            </div>
                        </div>

                        <div className="space-y-1">
                            <label className="font-label-caps text-label-caps text-on-surface block ml-1" htmlFor="confirmPassword">Confirmar Senha</label>
                            <div className="relative">
                                <KeyRound className="absolute left-3 top-1/2 -translate-y-1/2 text-outline-variant" size={20} />
                                <input className="w-full h-12 pl-10 pr-10 rounded-lg bg-surface-container-lowest border border-outline-variant text-on-surface placeholder:text-outline-variant input-focus-glow transition-all" id="confirmPassword" placeholder="••••••••" required type="password"/>
                            </div>
                        </div>

                        <div className="flex items-start gap-3 pt-2">
                             <div className="flex items-center h-6">
                                <input className="w-5 h-5 rounded border-outline-variant text-primary-container focus:ring-secondary-container bg-surface-container-lowest" id="terms" required type="checkbox"/>
                            </div>
                            <label className="font-body-md text-sm text-on-surface-variant leading-tight mt-0.5" htmlFor="terms">
                                Aceito os <a className="text-primary-container font-medium hover:underline underline-offset-2" href="#">Termos de Uso</a> e <a className="text-primary-container font-medium hover:underline underline-offset-2" href="#">Política de Privacidade</a>.
                            </label>
                        </div>

                        <button type="submit" className="w-full h-14 mt-6 bg-primary-container hover:bg-primary text-on-primary font-headline-md text-lg rounded-lg shadow-md hover:shadow-lg transition-all duration-300 flex items-center justify-center gap-2 group active:scale-[0.98]">
                            Criar minha conta
                            <ArrowRight className="group-hover:translate-x-1 transition-transform" />
                        </button>
                    </form>

                    <div className="mt-8 text-center">
                        <p className="font-body-md text-sm text-on-surface-variant">
                            Já possui conta? <a onClick={() => navigate('/login')} className="text-primary-container font-semibold hover:text-secondary-container transition-colors cursor-pointer">Fazer login</a>
                        </p>
                    </div>
                 </div>
            </main>
        </div>
    );
};
