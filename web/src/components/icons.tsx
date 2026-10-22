import type { ReactNode, SVGProps } from 'react';

type Props = SVGProps<SVGSVGElement> & { children?: ReactNode };

function Base({ children, ...props }: Props) {
  return (
    <svg
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth={1.7}
      strokeLinecap="round"
      strokeLinejoin="round"
      aria-hidden="true"
      focusable="false"
      {...props}
    >
      {children}
    </svg>
  );
}

export const IconPin = (props: Props) => (
  <Base {...props}>
    <path d="M12 21s7-5.35 7-11a7 7 0 1 0-14 0c0 5.65 7 11 7 11Z" />
    <circle cx="12" cy="10" r="2.5" />
  </Base>
);

export const IconCalendar = (props: Props) => (
  <Base {...props}>
    <rect x="3.5" y="5" width="17" height="15.5" rx="3" />
    <path d="M8 3.5V7M16 3.5V7M3.5 10h17" />
  </Base>
);

export const IconClock = (props: Props) => (
  <Base {...props}>
    <circle cx="12" cy="12" r="8.5" />
    <path d="M12 7.5V12l3 2" />
  </Base>
);

export const IconUsers = (props: Props) => (
  <Base {...props}>
    <circle cx="9" cy="8.5" r="3.3" />
    <path d="M3.5 19.5c0-3.2 2.5-5.2 5.5-5.2s5.5 2 5.5 5.2" />
    <path d="M16 5.6a3.1 3.1 0 0 1 0 6" />
    <path d="M17.5 14.6c2 .6 3.2 2.3 3.2 4.4" />
  </Base>
);

export const IconSearch = (props: Props) => (
  <Base {...props}>
    <circle cx="11" cy="11" r="6.5" />
    <path d="m16 16 4 4" />
  </Base>
);

export const IconPlus = (props: Props) => (
  <Base {...props}>
    <path d="M12 5v14M5 12h14" />
  </Base>
);

export const IconLogout = (props: Props) => (
  <Base {...props}>
    <path d="M15 4.5H6.5A2 2 0 0 0 4.5 6.5v11a2 2 0 0 0 2 2H15" />
    <path d="M14 12h7M18 8.5 21.5 12 18 15.5" />
  </Base>
);

export const IconClose = (props: Props) => (
  <Base {...props}>
    <path d="M6 6l12 12M18 6 6 18" />
  </Base>
);

export const IconCheck = (props: Props) => (
  <Base {...props}>
    <path d="m5 12.5 4.5 4.5L19 7" />
  </Base>
);

export const IconPlay = (props: Props) => (
  <Base {...props}>
    <path d="M8 5.5v13l10.5-6.5Z" />
  </Base>
);

export const IconFlag = (props: Props) => (
  <Base {...props}>
    <path d="M6 21V4" />
    <path d="M6 4.5h10.5l-2 4 2 4H6" />
  </Base>
);

export const IconBan = (props: Props) => (
  <Base {...props}>
    <circle cx="12" cy="12" r="8.5" />
    <path d="m6.2 6.2 11.6 11.6" />
  </Base>
);

export const IconSend = (props: Props) => (
  <Base {...props}>
    <path d="M20.5 3.5 10 14" />
    <path d="M20.5 3.5 14 20.5l-4-6.5-6.5-4Z" />
  </Base>
);

export const IconTrophy = (props: Props) => (
  <Base {...props}>
    <path d="M8 4.5h8v4a4 4 0 0 1-8 0Z" />
    <path d="M8 6H5.5a2.5 2.5 0 0 0 2.5 3.5M16 6h2.5A2.5 2.5 0 0 1 16 9.5" />
    <path d="M12 12.5V16M9 20h6M10 20l.6-4h2.8l.6 4" />
  </Base>
);

export const IconSparkles = (props: Props) => (
  <Base {...props}>
    <path d="M12 3.5 13.8 9l5.5 1.8-5.5 1.8L12 18l-1.8-5.4L4.7 10.8 10.2 9Z" />
    <path d="M18.5 16.5 19.3 19l2.2.8-2.2.8-.8 2.4-.8-2.4-2.2-.8 2.2-.8Z" />
  </Base>
);

export const IconMail = (props: Props) => (
  <Base {...props}>
    <rect x="3" y="5.5" width="18" height="13" rx="2.6" />
    <path d="m3.8 7 7.1 5.3a2 2 0 0 0 2.2 0L20.2 7" />
  </Base>
);

export const IconLock = (props: Props) => (
  <Base {...props}>
    <rect x="4.5" y="10" width="15" height="10" rx="2.6" />
    <path d="M8 10V7.8a4 4 0 0 1 8 0V10" />
  </Base>
);

export const IconArrowRight = (props: Props) => (
  <Base {...props}>
    <path d="M4.5 12h15M14 6.5 19.5 12 14 17.5" />
  </Base>
);

export const IconRefresh = (props: Props) => (
  <Base {...props}>
    <path d="M20 12a8 8 0 1 1-2.5-5.8" />
    <path d="M20 4.5V10h-5.5" />
  </Base>
);
