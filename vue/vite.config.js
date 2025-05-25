import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import ElementPlus from 'unplugin-element-plus/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    // 按需定制主题配置
    ElementPlus({
      useSource: true,
    }),
    AutoImport({
      resolvers: [ElementPlusResolver( { importStyle: 'sass'})],
    }),
    Components({
      resolvers: [ElementPlusResolver( { importStyle: 'sass'})],
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@use "@/assets/index.scss" as *;`,
      }
    }
  },
  server: {
    // host: 'localhost', // 允许局域网访问
    port: 8080,      // 确保端口固定
    strictPort: true, // 如果 5173 被占用，不随机分配新端口
    open: false,      // 关闭自动打开浏览器
    host: true, // ← 允許外部訪問
    allowedHosts: ['mgr.ntubbirc.ggff.net'], // ← 加入你的 Cloudflare 子網域

    proxy: {
      '/api': {
        // target: 'http://localhost:9090', // Spring Boot 后端地址
        target: 'https://black.ntubbirc.ggff.net',
        changeOrigin: true,
        //rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }

})
