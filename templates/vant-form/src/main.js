import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Vant, { ConfigProvider } from 'vant'

import 'vant/lib/index.css'

createApp(App).use(store).use(router).use(Vant).use(ConfigProvider).mount('#app')
