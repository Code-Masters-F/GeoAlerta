import { Leaf, Play, Check, Tractor } from 'lucide-react';
import { motion } from 'framer-motion';
import { useNavigate } from '../Router';
import { Button } from './ui/Button';
import { RiskTypeCard } from './ui/RiskTypeCard';
import { PlanCard } from './ui/PlanCard';
import { getTiposDeRisco } from '../data/tiposDeRisco';
import { getPlanos } from '../data/planos';

const fadeIn = {
  hidden: { opacity: 0, y: 20 },
  visible: { opacity: 1, y: 0, transition: { duration: 0.6 } },
};

const stagger = {
  hidden: { opacity: 0 },
  visible: { opacity: 1, transition: { staggerChildren: 0.12 } },
};

const indicadoresHero = [
  { valor: '7 tipos', label: 'de risco monitorados' },
  { valor: '+3.200', label: 'propriedades protegidas' },
  { valor: '98%', label: 'de precisao nos alertas' },
];

const diferenciais = [
  'Mapas de satelite diarios',
  'Historico climatico integrado',
  'Relatorios em PDF faceis de compartilhar',
];

export const LandingPage = () => {
  const navigate = useNavigate();
  const riscos = getTiposDeRisco();
  const planos = getPlanos();

  const scrollToSection = (id) => {
    document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <div className="min-h-screen bg-background font-sans text-on-background antialiased">
      <header className="sticky top-0 z-50 border-b border-outline-variant bg-surface-bright/80 backdrop-blur-md">
        <div className="mx-auto flex h-16 max-w-7xl items-center justify-between px-4 sm:px-6 lg:px-8">
          <button
            className="flex items-center gap-2"
            onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}
            aria-label="Inicio"
          >
            <Leaf className="h-7 w-7 text-primary" aria-hidden="true" />
            <span className="font-display text-xl font-bold text-on-surface">GeoAlerta</span>
          </button>
          <nav className="hidden items-center gap-8 md:flex">
            <button className="text-sm font-medium text-on-surface-variant transition-colors hover:text-on-surface" onClick={() => scrollToSection('funcionalidades')}>Funcionalidades</button>
            <button className="text-sm font-medium text-on-surface-variant transition-colors hover:text-on-surface" onClick={() => scrollToSection('riscos')}>Riscos</button>
            <button className="text-sm font-medium text-on-surface-variant transition-colors hover:text-on-surface" onClick={() => scrollToSection('planos')}>Planos</button>
          </nav>
          <div className="hidden items-center gap-3 md:flex">
            <button onClick={() => navigate('/login')} className="text-sm font-medium text-on-surface-variant transition-colors hover:text-on-surface">Entrar</button>
            <Button variant="primary" size="md" onClick={() => navigate('/cadastro')}>Comecar gratis</Button>
          </div>
        </div>
      </header>

      <main>
        {/* Hero */}
        <section
          className="relative flex min-h-[88vh] flex-col items-center justify-center overflow-hidden px-4 py-24 text-center sm:px-6 lg:px-8"
          style={{ backgroundImage: "url('https://images.unsplash.com/photo-1495107334309-fcf20504a5ab?q=80&w=3000&auto=format&fit=crop')", backgroundSize: 'cover', backgroundPosition: 'center' }}
        >
          <div className="absolute inset-0 bg-background/70 backdrop-blur-[2px]" aria-hidden="true" />

          <motion.div initial="hidden" animate="visible" variants={fadeIn} className="relative z-10 mb-8 inline-flex items-center gap-2 rounded-full bg-surface-bright/90 px-3 py-1 text-xs font-semibold tracking-wide text-on-surface-variant shadow-sm">
            <span className="h-2 w-2 rounded-full bg-primary" aria-hidden="true" />
            Solucao B2B para o agronegocio brasileiro
          </motion.div>

          <motion.h1 initial="hidden" animate="visible" variants={fadeIn} className="relative z-10 mb-6 max-w-4xl font-display text-4xl font-bold leading-[1.05] tracking-tight text-on-surface drop-shadow-sm sm:text-5xl lg:text-7xl">
            Proteja sua lavoura antes que o risco chegue
          </motion.h1>

          <motion.p initial="hidden" animate="visible" variants={fadeIn} className="relative z-10 mb-10 max-w-2xl text-lg font-medium leading-relaxed text-on-surface-variant drop-shadow-sm sm:text-xl">
            A GeoAlerta monitora sua propriedade em tempo real e envia alertas precisos sobre riscos climaticos, pragas e desastres, antes que causem prejuizo.
          </motion.p>

          <motion.div initial="hidden" animate="visible" variants={fadeIn} className="relative z-10 mb-16 flex w-full flex-col items-center justify-center gap-4 sm:flex-row">
            <Button variant="cta" size="lg" className="w-full sm:w-auto" onClick={() => navigate('/cadastro')}>
              <Tractor size={20} aria-hidden="true" /> Proteger minha fazenda
            </Button>
            <Button variant="light" size="lg" className="w-full sm:w-auto" onClick={() => scrollToSection('funcionalidades')}>
              <Play size={20} aria-hidden="true" /> Ver demonstracao
            </Button>
          </motion.div>

          <motion.div initial="hidden" whileInView="visible" viewport={{ once: true }} variants={stagger} className="relative z-10 flex flex-wrap justify-center gap-8 rounded-3xl border border-outline-variant bg-surface-bright/85 px-10 py-6 shadow-card backdrop-blur-md sm:gap-16">
            {indicadoresHero.map((item) => (
              <motion.div key={item.label} variants={fadeIn} className="text-center">
                <div className="tabular font-display text-3xl font-bold text-on-surface sm:text-4xl">{item.valor}</div>
                <div className="mt-1 text-sm font-medium text-on-surface-variant">{item.label}</div>
              </motion.div>
            ))}
          </motion.div>
        </section>

        {/* Riscos */}
        <section id="riscos" className="relative z-20 -mt-10 mx-auto max-w-7xl rounded-t-[3rem] bg-surface-bright px-4 py-20 shadow-[0_-10px_40px_rgba(11,40,24,0.04)] sm:px-6 lg:px-8">
          <SectionHeading sobre="Monitoramento completo" titulo="Todos os riscos que importam para sua lavoura" texto="Do essencial ao avancado, cobrimos os principais vetores de perda agricola no Brasil com precisao geoespacial." />
          <motion.div initial="hidden" whileInView="visible" viewport={{ once: true }} variants={stagger} className="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3">
            {riscos.map((risco) => (
              <motion.div key={risco.id} variants={fadeIn}>
                <RiskTypeCard risco={risco} />
              </motion.div>
            ))}
          </motion.div>
        </section>

        {/* Funcionalidades */}
        <section id="funcionalidades" className="border-t border-outline-variant bg-surface-bright px-4 py-20 sm:px-6 lg:px-8">
          <div className="mx-auto flex max-w-7xl flex-col items-center gap-12 md:flex-row">
            <motion.div initial={{ opacity: 0, x: -40 }} whileInView={{ opacity: 1, x: 0 }} viewport={{ once: true }} transition={{ duration: 0.7 }} className="flex-1">
              <img alt="Painel de controle exibindo a saude das culturas" className="w-full rounded-3xl border border-outline-variant shadow-card" src="https://images.unsplash.com/photo-1551288049-bebda4e38f71?q=80&w=2000&auto=format&fit=crop" loading="lazy" />
            </motion.div>
            <motion.div initial={{ opacity: 0, x: 40 }} whileInView={{ opacity: 1, x: 0 }} viewport={{ once: true }} transition={{ duration: 0.7 }} className="flex-1 space-y-6">
              <span className="text-xs font-bold uppercase tracking-widest text-primary">Visao inteligente</span>
              <h2 className="font-display text-3xl font-bold text-on-surface">Painel de controle com dados precisos</h2>
              <p className="leading-relaxed text-on-surface-variant">
                Agrupe todas as suas fazendas em uma unica tela. Acompanhe os alertas ativos, o nivel de severidade e tome decisoes com base nas melhores informacoes agrometeorologicas disponiveis no pais.
              </p>
              <ul className="space-y-4 pt-2">
                {diferenciais.map((item) => (
                  <li key={item} className="flex items-center gap-3">
                    <span className="flex h-8 w-8 items-center justify-center rounded-full bg-primary-fixed text-primary">
                      <Check size={16} aria-hidden="true" />
                    </span>
                    <span className="font-medium text-on-surface">{item}</span>
                  </li>
                ))}
              </ul>
            </motion.div>
          </div>
        </section>

        {/* Planos */}
        <section id="planos" className="bg-background px-4 py-20 sm:px-6 lg:px-8">
          <div className="mx-auto max-w-7xl">
            <SectionHeading sobre="Planos e precos" titulo="Protecao para cada tamanho de operacao" texto="Comece pequeno e escale conforme sua operacao cresce. Sem fidelidade minima nos primeiros 3 meses." />
            <motion.div initial="hidden" whileInView="visible" viewport={{ once: true }} variants={stagger} className="mx-auto grid max-w-5xl grid-cols-1 items-center gap-8 md:grid-cols-3">
              {planos.map((plano) => (
                <motion.div key={plano.id} variants={fadeIn}>
                  <PlanCard plano={plano} onSelect={() => navigate('/cadastro')} />
                </motion.div>
              ))}
            </motion.div>
          </div>
        </section>

        {/* Contato */}
        <section id="contato" className="bg-agro-dark px-4 py-20 text-center">
          <motion.div initial={{ opacity: 0, scale: 0.95 }} whileInView={{ opacity: 1, scale: 1 }} viewport={{ once: true }} transition={{ duration: 0.6 }} className="mx-auto max-w-3xl">
            <div className="mb-6 inline-flex h-16 w-16 items-center justify-center rounded-full bg-white/10 text-white">
              <Tractor className="h-8 w-8" aria-hidden="true" />
            </div>
            <h2 className="mb-6 font-display text-3xl font-bold text-white sm:text-4xl">Comece a proteger sua lavoura hoje mesmo</h2>
            <Button variant="cta" size="lg" onClick={() => navigate('/cadastro')}>Criar conta gratuita</Button>
          </motion.div>
        </section>
      </main>

      <footer className="bg-agro-dark px-4 py-8 text-center text-sm text-white/60">
        <p>GeoAlerta — Monitoramento de risco para o agronegocio brasileiro</p>
      </footer>
    </div>
  );
};

function SectionHeading({ sobre, titulo, texto }) {
  return (
    <motion.div initial="hidden" whileInView="visible" viewport={{ once: true }} variants={fadeIn} className="mb-16 text-center">
      <div className="mb-3 text-xs font-bold uppercase tracking-widest text-primary">{sobre}</div>
      <h2 className="mb-4 font-display text-3xl font-bold text-on-surface sm:text-4xl">{titulo}</h2>
      <p className="mx-auto max-w-2xl text-on-surface-variant">{texto}</p>
    </motion.div>
  );
}
