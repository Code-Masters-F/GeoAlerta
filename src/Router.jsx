import { createContext, useContext, useState } from 'react';

const RouterContext = createContext({
  currentRoute: '/',
  navigate: () => {},
});

export const RouterProvider = ({ children }) => {
  const [currentRoute, setCurrentRoute] = useState('/');
  return (
    <RouterContext.Provider value={{ currentRoute, navigate: setCurrentRoute }}>
      {children}
    </RouterContext.Provider>
  );
};

export const useNavigate = () => {
  const { navigate } = useContext(RouterContext);
  return navigate;
};

export const useCurrentRoute = () => {
  const { currentRoute } = useContext(RouterContext);
  return currentRoute;
};
