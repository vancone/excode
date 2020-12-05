<template>
  <div class="left-bar">
    <el-tabs tab-position="left" style="height: 100%;border-right:solid 1px #ccc;">
    <el-tab-pane label="Project">
      <template #label>
        <span><i class="el-icon-copy-document"></i></span>
      </template>
      用户管理</el-tab-pane>
    <el-tab-pane label="Data Source">
      <template #label>
        <span><i class="el-icon-coin"></i></span>
      </template>
      配置管理</el-tab-pane>
    <el-tab-pane label="Host">
      <template #label>
        <span><i class="el-icon-office-building"></i></span>
      </template>
      角色管理</el-tab-pane>
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
export default {
  name: 'HelloWorld',
  props: {
    msg: String
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="stylus">
.left-bar
  height 100%
  width 300px

h3
  margin 40px 0 0

ul
  list-style-type none
  padding 0

li
  display inline-block
  margin 0 10px

a
  color #42b983
</style>
