<template>
  <el-menu
    :default-active="activeIndex"
    class="menu"
    :default-openeds="['/editor/data-access']"
    router
  >
    <el-menu-item index="/editor/overview">
      <el-icon size="15"><data-line /></el-icon>
      <span>Overview</span>
    </el-menu-item>
    <el-sub-menu index="/editor/data-access">
      <template #title>
        <el-icon size="15"><pie-chart /></el-icon>
        <span>Data Access</span>
      </template>
      <el-menu-item index="/editor/solution">Solution</el-menu-item>
      <el-menu-item index="/editor/api-group">API Group</el-menu-item>
      <el-menu-item index="/editor/data-store">Data Store</el-menu-item>
    </el-sub-menu>
    <el-menu-item index="presentation">
      <el-icon size="15"><Picture /></el-icon>
      <span>Presentation</span>
    </el-menu-item>
    <el-menu-item index="presentation">
      <el-icon size="15"><Document /></el-icon>
      <span>Documents</span>
    </el-menu-item>
  </el-menu>
</template>

<script lang="ts">
import { DataLine, Document, Picture, PieChart } from "@element-plus/icons";
import { defineComponent } from "vue";
import { useRoute } from 'vue-router';
export default defineComponent({
  components: {
    DataLine,
    Document,
    Picture,
    PieChart,
  },
  setup() {
    const activeIndex = useRoute().path;

    return {
      activeIndex
    }
  },
});
</script>

<style scoped>
.menu {
  width: 200px;
}
:deep(.el-menu-item) {
  display: flex;
  height: 45px;
  align-items: center;
}
:deep(.el-icon) {
  margin-right: 5px;
}
:deep(.el-menu-item.is-active) {
  background-color: #e6e9d2;
  color: #666;
}
:deep(.el-sub-menu__title) {
  height: 45px;
}
</style>
