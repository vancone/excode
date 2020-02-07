import Vue from 'vue'
import Router from 'vue-router'
import InitialPage from '@/components/InitialPage'
import Project from '@/components/Project'
import Overview from '@/components/Overview'
import Dependencies from '@/components/Dependencies'
import DataTables from '@/components/DataTables'
import Configuration from '@/components/Configuration'
import Build from '@/components/Build'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/project'
    },
    {
      path: '/overview',
      name: 'Overview',
      component: Overview
    },
    {
      path: '/dependencies',
      name: 'Dependencies',
      component: Dependencies
    },
    {
      path: '/datatables',
      name: 'datatables',
      component: DataTables
    },
    {
      path: '/project',
      name: 'Project',
      component: Project
    },
    {
      path: '/configuration',
      name: 'configuration',
      component: Configuration
    },
    {
      path: '/build',
      name: 'build',
      component: Build
    }
  ]
})
