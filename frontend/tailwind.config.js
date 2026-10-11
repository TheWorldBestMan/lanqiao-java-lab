/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        ink: '#1d2530',
        paper: '#f5f3ed',
        signal: '#ef6b3b',
        teal: '#087e72',
        moss: '#a7c957',
      },
      boxShadow: { card: '0 16px 45px rgba(31, 39, 48, 0.08)' },
    },
  },
  plugins: [],
}

