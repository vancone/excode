import Vue from 'vue'
import Router from 'vue-router'
import Initial from '@/components/Initial'
import Editor from '@/components/Editor'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Initial',
      component: Initial
    },
    {
      path: '/edit',
      name: 'Editor',
      component: Editor
    },
  ]
})
