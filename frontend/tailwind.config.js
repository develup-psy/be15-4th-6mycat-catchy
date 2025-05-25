/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Pretendard', 'system-ui', 'sans-serif'],
        serif: ['Pretendard', 'serif'],
        mono: ['Pretendard', 'monospace'],
      },
      colors: {
        // 🎨 Brand Colors
        primary: '#FF5C8D',
        'primary-hover': '#e04f7e',
        'primary-disabled': '#ffbcd1',
        'primary-light': '#ffe9f0',

        secondary: '#FFC857',
        'secondary-hover': '#f6b742',
        'secondary-light': '#fff4dc',

        // ⚪ Neutral Colors
        white: '#ffffff',
        black: '#000000',
        'gray-100': '#f9f9f9',
        'gray-200': '#eeeeee',
        'gray-300': '#cccccc',
        'gray-400': '#999999',
        'gray-500': '#666666',
        'gray-600': '#444444',
        'gray-700': '#2e2e2e',

        // 🚨 상태 색상
        error: '#FF4D4F',
        'error-light': '#ffe6e6',
        success: '#52C41A',
        'success-light': '#e6ffe6',
        warning: '#FAAD14',
        'warning-light': '#fff8e6',

        // 💡 상태용 추가 컬러
        'focus-outline': '#FFAEC4',
        'disabled-bg': '#f5f5f5',
        'disabled-text': '#999999',

        // Overlay (rgba)
        'black-alpha-05': 'rgba(0, 0, 0, 0.05)',
        'black-alpha-10': 'rgba(0, 0, 0, 0.1)',
        'black-alpha-20': 'rgba(0, 0, 0, 0.2)',
        'black-alpha-40': 'rgba(0, 0, 0, 0.4)',
        'black-alpha-60': 'rgba(0, 0, 0, 0.6)',
      },
      borderRadius: {
        sm: '4px',
        md: '8px',
        lg: '12px',
        full: '9999px',
      },
      boxShadow: {
        none: 'none',
        base: '0 1px 3px rgba(0, 0, 0, 0.1)',
        hover: '0 4px 6px rgba(0, 0, 0, 0.15)',
        elevated: '0 8px 16px rgba(0, 0, 0, 0.2)',
      },
      fontSize: {
        // Display
        'display-xl': ['72px', { lineHeight: '74px' }],
        'display-lg': ['64px', { lineHeight: '70px' }],
        'display-md': ['48px', { lineHeight: '54px' }],
        'display-sm': ['40px', { lineHeight: '44px' }],

        // Headline
        'headline-lg': ['32px', { lineHeight: '40px' }],
        'headline-md': ['28px', { lineHeight: '36px' }],
        'headline-sm': ['24px', { lineHeight: '32px' }],

        // Body
        'body-lg': ['18px', { lineHeight: '28px' }],
        'body-md': ['16px', { lineHeight: '24px' }],
        'body-sm': ['14px', { lineHeight: '20px' }],

        // Caption
        caption: ['12px', { lineHeight: '16px' }],
      },
    },
  },
  plugins: [],
};
