<template>
  <div class="data-access-panel">
    <el-tabs type="card" class="tabs">
      <el-tab-pane label="Solution" style="height: 100%; overflow: scroll">
        <Solution />
      </el-tab-pane>
      <el-tab-pane label="API">API</el-tab-pane>
      <el-tab-pane label="Data Store">
        <DataStore />
      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script>
import { defineComponent } from 'vue'
import Solution from '~/components/data-access/components/Solution.vue'
import DataStore from '~/components/data-access/components/DataStore.vue'
export default defineComponent({
  name: 'DataAccessPanel',
  components: {
    DataStore, Solution
  }
})
</script>

<style scoped>
.data-access-panel {
  height: calc(100% - 20px);
  overflow: auto;
}
:deep(.el-tabs__nav-scroll) {
  background: #f9f9fa;
  height: 30px;
  border-bottom: solid 1px #ddd;
}
:deep(.el-tabs--card > .el-tabs__header .el-tabs__item) {
  height: 30px;
  line-height: 30px;

}
:deep(.el-tabs--card > .el-tabs__header .el-tabs__item) {
  border-left: none;
  border-right: solid 1px #ddd;
}
:deep(.el-tabs__item.is-active) {
  border-right: solid 1px #ddd;
  background-color: white;
}
:deep(.el-tabs__header) {
  margin: 0;
}
:deep(.el-tabs--card > .el-tabs__header .el-tabs__nav) {
  border: none;
}
:deep(.el-table td) {
  padding: 5px;
}
.tabs {
  height: inherit;
  overflow: hidden;
  padding: 0;
  margin: 0;
}
</style>
