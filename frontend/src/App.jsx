import { MotionConfig } from 'framer-motion';
import { RouterProvider, useCurrentRoute } from './Router';
import { LandingPage } from './components/LandingPage';
import { LoginPage } from './components/LoginPage';
import { CadastroPage } from './components/CadastroPage';
import { DashboardPage } from './components/DashboardPage';
import { MapaPage } from './components/MapaPage';
import { PropriedadesPage } from './components/PropriedadesPage';
import { PreferenciasPage } from './components/PreferenciasPage';

const pages = {
  '/': LandingPage,
  '/login': LoginPage,
  '/cadastro': CadastroPage,
  '/dashboard': DashboardPage,
  '/mapa': MapaPage,
  '/propriedades': PropriedadesPage,
  '/preferencias': PreferenciasPage,
};

const AppContent = () => {
  const currentRoute = useCurrentRoute();
  const Page = pages[currentRoute] ?? LandingPage;
  return <Page />;
};

export default function App() {
  return (
    <MotionConfig reducedMotion="user">
      <div className="min-h-screen w-full">
        <RouterProvider>
          <AppContent />
        </RouterProvider>
      </div>
    </MotionConfig>
  );
}
