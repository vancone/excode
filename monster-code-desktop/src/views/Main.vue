<template>
  <div class="main">
    <el-tabs
      tab-position="left"
      style="height: 100%; border-right: solid 1px #ccc"
    >
      <el-tab-pane label="Project">
        <template #label>
          <span><i class="el-icon-copy-document"></i></span>
        </template>
        用户管理</el-tab-pane
      >
      <el-tab-pane label="Data Source">
        <template #label>
          <span><i class="el-icon-coin"></i></span>
        </template>

        </el-tab-pane
      >
      <el-tab-pane label="Host" style="height:100%">
        <template #label>
          <span><i class="el-icon-office-building"></i></span>
        </template>
        <host/>
        </el-tab-pane>
      <el-tab-pane label="Settings">
        <template #label>
          <span><i class="el-icon-setting"></i></span>
        </template>
        定时任务补偿
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import Host from '@/views/Host.vue'

export default {
  name: 'Main',
  components: {
    Host
  }
}
</script>

<style scoped>
.left-bar {
  height: 100%;
  width: 300px;
}

h3 {
  margin: 40px 0 0;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

.main {
  height: 100%;
  text-align: left;
}

.main /deep/ .el-tabs__content {
  height: 100%;

}

.main /deep/ .el-tabs--left .el-tabs__header.is-left {
  margin-right: 0;
}
</style>
