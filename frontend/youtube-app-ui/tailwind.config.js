/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      width: {
        '65p' : '65%',
        '5p' : '5%',
        '30p' : '30%'
      }
    },
  },
  plugins: [],
};
