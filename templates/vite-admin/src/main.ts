import { createApp } from 'vue'
import App from './App.vue'
import i18n from '@/lang/index'
import router from '@/router/index'
import ElementPlus from 'element-plus'
import 'element-plus/lib/theme-chalk/index.css'
import '@/styles/index.scss'

const app = createApp(App)

const options = {
    size: 'mini'
}

app
    .use(ElementPlus, { ...options, i18n: i18n.global.t })
    .use(router)
    .use(i18n)
    .mount('#app')
