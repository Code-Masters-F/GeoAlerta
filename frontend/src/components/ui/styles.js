/**
 * Mapas de estilo semantico. Todas as classes sao escritas como literais
 * completos para que o scanner do Tailwind as inclua no bundle final
 * (classes montadas por concatenacao em runtime nao sao detectadas).
 */

export const toneConfig = {
  primary: { chip: 'bg-primary-fixed text-primary', bar: 'bg-primary' },
  warning: { chip: 'bg-warning-container text-warning', bar: 'bg-warning' },
  error: { chip: 'bg-error-container text-error', bar: 'bg-error' },
  info: { chip: 'bg-info-container text-info', bar: 'bg-info' },
  success: { chip: 'bg-success-container text-success', bar: 'bg-success' },
};

export const severityConfig = {
  critico: {
    label: 'Critico',
    solid: 'bg-error',
    text: 'text-error',
    card: 'bg-error-container/50 border-error/25',
    badge: 'bg-surface-bright text-error border-error/30',
    topBorder: 'border-error',
  },
  atencao: {
    label: 'Atencao',
    solid: 'bg-warning',
    text: 'text-warning',
    card: 'bg-warning-container/50 border-warning/25',
    badge: 'bg-surface-bright text-warning border-warning/30',
    topBorder: 'border-warning',
  },
  normal: {
    label: 'Normal',
    solid: 'bg-success',
    text: 'text-success',
    card: 'bg-success-container/50 border-success/25',
    badge: 'bg-surface-bright text-success border-success/30',
    topBorder: 'border-success',
  },
};

export function tone(name) {
  return toneConfig[name] ?? toneConfig.primary;
}

export function severity(name) {
  return severityConfig[name] ?? severityConfig.normal;
}
