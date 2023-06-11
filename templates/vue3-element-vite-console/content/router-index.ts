import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
${imports}

declare module 'vue-router' {
  interface RouteMeta {
    title: string;
    hidden?: boolean;
    icon?: string;
    activeMenu?: string;
  }
}

export const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    redirect: '/${modelName}',
    meta: {
      title: 'Home',
      hidden: true,
    }
  },
${routes}
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
