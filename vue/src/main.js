import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './style.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as ElementPlusIconVue from '@element-plus/icons-vue'

import '@/assets/global.css'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconVue)) {
    app.component(key, component)
}

app.use(router)
app.use(ElementPlus, { locale: zhCn })


app.mount('#app')
