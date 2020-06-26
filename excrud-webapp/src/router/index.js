import Vue from 'vue'
import Router from 'vue-router'
import Initial from '@/components/Initial'
import Editor from '@/components/Editor'
import Hosts from '@/components/hosts/index'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/projects',
      name: 'Initial',
      component: Initial
    },
    {
      path: '/edit',
      name: 'Editor',
      component: Editor
    },
    {
      path: '/hosts',
      name: 'Hosts',
      component: Hosts
    }
  ]
})
