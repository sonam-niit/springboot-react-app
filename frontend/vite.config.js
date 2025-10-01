import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true,      // required for Docker
    port: 8100,      // change Vite port
    allowedHosts: true
  },
})
