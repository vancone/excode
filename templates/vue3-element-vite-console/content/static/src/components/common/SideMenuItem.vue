<template>
  <el-sub-menu v-if="route.children && route.children.length > 0" :index="route.name">
    <template #title>
      <!-- <i class="icon-nav" :class="route.meta.icon"/> -->
      <span>{{ $t(route.meta?.title ?? '') }}</span>
    </template>
    <SideMenuItem v-for="sub in visibleChildrenFilter(route.children)" :key="sub.path" :route="sub" />
  </el-sub-menu>
  <el-menu-item v-else :index="route.meta?.activeMenu">
    <!-- <i class="icon-nav" :class="route.meta.icon || ''" /> -->
    <template #title>
      <span>{{ $t(route.meta?.title ?? '') }}</span>
    </template>
  </el-menu-item>
</template>

<script setup lang="ts">
import { RouteRecordRaw } from 'vue-router';
import { visibleChildrenFilter } from '~/util';

const props = defineProps<{
  route: RouteRecordRaw,
}>();
</script>

<style lang="scss" scoped>
@import '~/styles/_variables.scss';

.el-menu {
  .el-menu-item {
    &.is-active {
      background-color: $color-bg-menu-active !important;
    }

    &:hover {
      background-color: $color-bg-menu-active I !important;

      >i {
        color: inherit;
      }
    }
  }
}

// .el-menu-vertical-demo {
//   &:not(.el-menu--collapse) {
//     width: 200px;
//   }
// }
</style>

<style lang="scss">
.el-menu--collapse {
  >.sidemenu-item {
    .el-submenu__title {

      >span {
        height: 0;
        width: 0;
        overflow: hidden;
        visibility: hidden;
        display: inline-block;
      }

      >.el-submenu__icon-arrow {
        display: none;
      }
    }
  }
}
</style>