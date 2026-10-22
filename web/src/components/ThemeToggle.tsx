import type { Tema } from '../tema';
import { IconLuna, IconSol } from './icons';

interface Props {
  tema: Tema;
  onAlternar: () => void;
  className?: string;
}

export function ThemeToggle({ tema, onAlternar, className }: Props) {
  const esClaro = tema === 'claro';
  const etiqueta = esClaro ? 'Activar tema oscuro' : 'Activar tema claro';

  return (
    <button
      type="button"
      className={className ? `boton-icono boton-tema ${className}` : 'boton-icono boton-tema'}
      onClick={onAlternar}
      aria-label={etiqueta}
      title={etiqueta}
      aria-pressed={esClaro}
    >
      {esClaro ? <IconLuna width={18} height={18} /> : <IconSol width={18} height={18} />}
    </button>
  );
}
