import Vue from 'vue'
import Router from 'vue-router'
import Welcome from '@/views/Welcome'
import Login from '@/views/account/Login'
import Project from '@/views/project/Project'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Welcome',
      component: Welcome
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/project',
      name: 'Project',
      component: Project
    }
  ]
})
