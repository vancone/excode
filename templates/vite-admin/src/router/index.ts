import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router"
import Building from '@/components/building/index.vue'
import Dashboard from '@/views/dashboard/index.vue'
import ProgramManagement from '@/views/program/index.vue'

export const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'home',
        redirect: '/dashboard',
        meta: {
            hidden: true
        }
    },
    {
        path: '/dashboard',
        name: 'dashboard',
        component: Dashboard,
        meta: {
            title: '首页',
            activeMenu: '/dashboard',
            icon: 'el-icon-s-home'
        }
    },
    {
        path: '/segment',
        name: 'segment',
        component: ProgramManagement,
        meta: {
            title: '代码段管理',
            activeMenu: '/segment',
            icon: 'el-icon-tickets'
        }
    },
    {
        path: '/project',
        name: 'project',
        component: ProgramManagement,
        meta: {
            title: '项目管理',
            activeMenu: '/project',
            icon: 'el-icon-tickets'
        }
    },
    {
        path: '/history',
        name: 'history',
        component: Building,
        meta: {
            title: '运行历史',
            activeMenu: '/history',
            icon: 'el-icon-timer'
        }
    }
]
const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router