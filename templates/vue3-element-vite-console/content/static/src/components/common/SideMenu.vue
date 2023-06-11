<template>
  <div class="menu-wrapper">
    <h1 v-if="title !== undefined && title !== ''">{{ title }}</h1>
    <el-menu class="menu" :default-active="activeMenu" :default-openeds="['table']" router>
      <side-menu-item v-for="menu in menus" :key="menu.path" :route="menu" />
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useRoute } from "vue-router";
import { routes } from "~/router";
import { visibleChildrenFilter } from "~/util";
import SideMenuItem from "./SideMenuItem.vue";

const props = defineProps<{
  title: String
}>();

const route = useRoute();
const menus = visibleChildrenFilter(routes);
const activeMenu = computed(() => route.meta.activeMenu);
</script>

<style lang="scss" scoped>
.menu-wrapper {
  height: 100%;
  background: white;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);

  h1 {
    color: #222;
    font-size: 14px;
    font-weight: normal;
    border-bottom: solid 1px #ddd;
    padding: 20px 0;
    margin: 0 20px 10px 20px;

  }
}

.menu {
  width: 200px;
  border-right: none;
}

:deep(.el-menu-item) {
  display: flex;
  height: 36px;
  align-items: center;
  font-size: 12px;
  margin-left: 3px;
  border-left: solid 3px white;
}

:deep(.el-icon) {
  margin-right: 5px;
}

:deep(.el-menu-item.is-active) {
  background-color: #e6e9d2;
  color: #666;
  border-left: solid 3px #8ba74f;
}

:deep(.el-sub-menu__title) {
  height: 36px;
  font-size: 12px;
  margin-left: 5px;
}
</style>
