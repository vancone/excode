import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import styleImport from 'vite-plugin-style-import'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    styleImport({
      libs: [
        {
          libraryName: 'element-plus',
          esModule: true,
          ensureStyleFile: true,
          resolveStyle: (name) => {
            const fileName = name.slice(3)
            return `element-plus/packages/theme-chalk/src/${fileName}.scss`
          },
          resolveComponent: (name) => `element-plus/lib/${name}`
        }
      ]
    })
  ],
  css: {
    preprocessorOptions: {
      sass: {}
    }
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    host: 'localhost',
    proxy: {
      '/api': 'http://baidu.com/'
    }
  },
  build: {
    sourcemap: true
  }
})
