<template>
  <el-menu
    :default-active="activeIndex"
    class="menu"
    router
  >
    <el-menu-item :index="`../overview/${projectId}`">
      <el-icon size="15"><data-line/></el-icon>Overview</el-menu-item>
    <el-menu-item :index="`../data-access/${projectId}`"><el-icon size="15"><pie-chart/></el-icon>Data Access</el-menu-item>
    <el-menu-item index="presentation"><el-icon size="15"><Picture/></el-icon>Presentation</el-menu-item>
    <el-menu-item index="presentation"><el-icon size="15"><Document/></el-icon>Documents</el-menu-item>
  </el-menu>
</template>

<script lang="ts">
import { DataLine, Document, Picture, PieChart } from '@element-plus/icons'
import { defineComponent } from 'vue'
import { useRoute } from 'vue-router'
export default defineComponent({
  components: {
    DataLine, Document, Picture, PieChart
  },
  setup() {
    const projectId = useRoute().params.projectId;
    const activeIndex = `../overview/${projectId}`

    return {
      activeIndex,
      projectId
    }
  }
})
</script>

<style scoped>
.menu {
  width: 200px;
}
.icon {
    height: 18px;
    margin-right: 10px;
}
:deep(.el-menu-item) {
  display: flex;
  height: 45px;
  align-items: center;
}
:deep(.el-icon) {
  margin-right: 5px;
}
:deep(.is-active) {
  background-color: #8ba74f;
  color: white;
}
</style>
