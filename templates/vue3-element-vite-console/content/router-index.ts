import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import Dashboard from '../views/dashboard/index.vue'
import TableView from '../views/TableView.vue';
import Building from '../components/common/Building.vue';
import Secondary from '../components/common/Secondary.vue';

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
    redirect: '/overview',
    meta: {
      title: 'Home',
      hidden: true,
    }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: {
      title: 'menu.dashboard',
      activeMenu: '/dashboard',
      icon: 'el-icon-data-analysis',
    }
  },
${routes}
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
