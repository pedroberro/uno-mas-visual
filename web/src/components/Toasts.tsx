export interface Toast {
  id: string;
  mensaje: string;
  tono: 'ok' | 'error';
}

interface Props {
  toasts: Toast[];
}

export function Toasts({ toasts }: Props) {
  return (
    <div className="toasts" aria-live="polite" aria-atomic="false">
      {toasts.map((toast) => (
        <div key={toast.id} className={`toast toast--${toast.tono}`}>
          {toast.mensaje}
        </div>
      ))}
    </div>
  );
}
