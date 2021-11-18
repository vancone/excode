<template>
  <el-menu
    :default-active="activeIndex"
    class="menu"
    router
    @open="handleOpen"
    @close="handleClose"
  >
    <el-menu-item :index="`../overview/${projectId}`">
      <el-icon size="15"><data-line/></el-icon>Overview</el-menu-item>
    <el-menu-item :index="`../data-access/${projectId}`"><el-icon size="15"><pie-chart/></el-icon>Data Access</el-menu-item>
    <el-menu-item index="presentation"><img class="icon" src="./../../assets/vuejs.svg">Presentation</el-menu-item>
    <el-menu-item index="presentation"><img class="icon" src="./../../assets/vuejs.svg">Documents</el-menu-item>
  </el-menu>
</template>

<script>
import { DataLine, PieChart } from '@element-plus/icons'
import { defineComponent } from 'vue'
import { useRoute } from 'vue-router'
export default defineComponent({
  components: {
    DataLine, PieChart
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
  align-items: center;
}
:deep(.el-icon) {
  margin-right: 5px;
}
</style>
