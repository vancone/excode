import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Main',
    component: () => import('../views/Main')
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
