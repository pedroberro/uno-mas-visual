import { useEffect, type ReactNode } from 'react';
import { IconClose } from './icons';

interface Props {
  titulo: string;
  subtitulo?: string;
  onCerrar: () => void;
  children: ReactNode;
  amplio?: boolean;
}

export function Modal({ titulo, subtitulo, onCerrar, children, amplio = false }: Props) {
  useEffect(() => {
    const alPresionarTecla = (evento: KeyboardEvent) => {
      if (evento.key === 'Escape') onCerrar();
    };
    document.addEventListener('keydown', alPresionarTecla);
    document.body.classList.add('sin-scroll');
    return () => {
      document.removeEventListener('keydown', alPresionarTecla);
      document.body.classList.remove('sin-scroll');
    };
  }, [onCerrar]);

  return (
    <div className="modal-fondo" role="presentation" onMouseDown={onCerrar}>
      <section
        className={amplio ? 'modal modal--amplio' : 'modal'}
        role="dialog"
        aria-modal="true"
        aria-label={titulo}
        onMouseDown={(evento) => evento.stopPropagation()}
      >
        <header className="modal__cabecera">
          <div>
            <h2>{titulo}</h2>
            {subtitulo && <p>{subtitulo}</p>}
          </div>
          <button type="button" className="boton-icono" onClick={onCerrar} aria-label="Cerrar">
            <IconClose width={18} height={18} />
          </button>
        </header>
        <div className="modal__cuerpo">{children}</div>
      </section>
    </div>
  );
}
