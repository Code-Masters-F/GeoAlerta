import {
  ThermometerSun,
  CloudRain,
  Snowflake,
  Flame,
  Bug,
  Tractor,
  Bell,
  AlertTriangle,
  Sprout,
  Droplets,
  Leaf,
} from 'lucide-react';

/**
 * Registro de icones. Os dados de dominio referenciam um icone por string
 * (ex.: 'flame'), mantendo a camada de dados livre de dependencia de UI.
 */
const registry = {
  'thermometer-sun': ThermometerSun,
  'cloud-rain': CloudRain,
  snowflake: Snowflake,
  flame: Flame,
  bug: Bug,
  tractor: Tractor,
  bell: Bell,
  'alert-triangle': AlertTriangle,
  sprout: Sprout,
  droplets: Droplets,
  leaf: Leaf,
};

export function Icon({ name, ...props }) {
  const Glyph = registry[name] ?? Leaf;
  return <Glyph aria-hidden="true" {...props} />;
}
