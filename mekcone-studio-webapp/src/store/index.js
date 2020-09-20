import Vue from 'vue'
import Vuex from 'vuex'

import project from './modules/project'

// dynamic router permission control (Experimental)
// import permission from './modules/async-router'
import getters from './getters'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    project
  },
  state: {

  },
  mutations: {

  },
  actions: {

  },
  getters
})
