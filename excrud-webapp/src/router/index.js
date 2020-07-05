import Vue from 'vue'
import Router from 'vue-router'
import Projects from '@/components/projects/index'
import ProjectEditor from '@/components/projects/editor'
import Hosts from '@/components/hosts/index'
import HostPane from '@/components/hosts/host-pane'
import Settings from '@/components/settings/index'
import SpringBootModuleExtensions from '@/components/projects/modules/spring-boot/spring-boot-extensions'
// import { component } from 'vue/types/umd'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/projects/editor',
      name: 'ProjectEditor',
      component: ProjectEditor,
      children: [
        {
          path: 'mod-sb-ext',
          component: SpringBootModuleExtensions
        }
      ]
    },
    {
      path: '/projects',
      name: 'Projects',
      component: Projects
    },
    {
      path: '/hosts/pane',
      name: 'HostPane',
      component: HostPane
    },
    {
      path: '/hosts',
      name: 'Hosts',
      component: Hosts
      /* children: [
        {
          path: 'pane',
          name: 'HostPane',
          component: HostPane
        }
      ] */
    },

    {
      path: '/settings',
      name: 'Settings',
      component: Settings
    },
    {
      path: '/',
      redirect: '/projects'
    }
  ]
})
