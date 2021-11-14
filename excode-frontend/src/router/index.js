import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/editor',
    name: 'Editor',
    component: () => import('../views/Editor.vue'),
    children: [
      {
        path: '',
        redirect: '/editor/overview'
      },
      {
        path: 'overview',
        component: () => import('../components/editor/Overview.vue')
      },
      {
        path: 'data-access',
        component: () => import('../components/data-access/DataAccessPanel.vue')
      },
      {
        path: 'raw',
        component: () => import('../views/RawViewer.vue')
      }
    ]
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
