const base =
  'inline-flex items-center justify-center gap-2 rounded-full font-semibold transition-all ' +
  'focus-visible:outline-none focus-visible:ring-4 focus-visible:ring-primary/25 ' +
  'active:scale-[0.98] disabled:opacity-50 disabled:pointer-events-none';

const sizes = {
  md: 'px-6 py-3 text-sm',
  lg: 'px-8 py-3.5 text-base',
};

const variants = {
  // Acao primaria: verde profundo da marca.
  primary: 'bg-primary-container text-on-primary shadow-soft hover:bg-primary hover:shadow-card hover:-translate-y-0.5',
  // Acento premium (harvest gold): reservado para a conversao principal.
  cta: 'bg-cta text-on-cta shadow-gold hover:bg-cta-hover hover:-translate-y-0.5',
  // Secundaria sobre fundo claro.
  outline: 'border border-primary-container text-primary-container hover:bg-primary-container hover:text-on-primary',
  // Secundaria sobre fundo escuro/imagem.
  light: 'bg-surface-bright/90 text-primary-container backdrop-blur border border-outline-variant hover:bg-surface-bright',
};

export function Button({ as = 'button', variant = 'primary', size = 'md', className = '', children, ...props }) {
  const Tag = as;
  const classes = `${base} ${sizes[size] ?? sizes.md} ${variants[variant] ?? variants.primary} ${className}`;
  return (
    <Tag className={classes} {...props}>
      {children}
    </Tag>
  );
}
