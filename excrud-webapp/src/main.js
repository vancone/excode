// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
// import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import VueAxios from 'vue-axios'
import '@/styles/element-variables.scss'
import locale from 'element-ui/lib/locale/lang/en'
import Icon from 'vue2-svg-icon/Icon'

import utils from './utils'

Vue.prototype.$utils = utils

Vue.config.productionTip = false

Vue.use(ElementUI, { locale })
Vue.use(VueAxios, axios)
Vue.component('icon', Icon)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
