// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'

import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'

import VueAxios from './utils/request'

import store from './store'

Vue.config.productionTip = false

Vue.use(Antd, VueAxios)

/* eslint-disable no-new */
/* new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
}) */
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
