import { Sidebar } from './Sidebar';
import { BottomBar } from './BottomBar';

export const Layout = ({ children, hideNav = false }) => {
  return (
    <div className="bg-background text-on-background min-h-screen flex flex-col md:flex-row font-body-md overflow-x-hidden">
        {!hideNav && <Sidebar />}
        <main className={`flex-1 flex flex-col min-h-screen w-full relative ${!hideNav ? 'lg:pl-64 pb-16 lg:pb-0' : ''}`}>
           {children}
        </main>
        {!hideNav && <BottomBar />}
    </div>
  )
}
