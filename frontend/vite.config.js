import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true,      // required for Docker
<<<<<<< HEAD
    port: 8100,      // change Vite port
    allowedHosts: true
=======
    port: 8082,      // change Vite port
>>>>>>> parent of f24d637 (port updated for frontend)
  },
})
