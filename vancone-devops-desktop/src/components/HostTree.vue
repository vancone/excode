<template>
  <div class="host-tree">
    <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
  </div>
</template>

<script>
export default {
  data () {
    return {
      data: [{
        label: 'Default Group',
        children: [{
          label: 'MekCone Server 1 (10.0.0.1)'
        }]
      }],
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    }
  },
  methods: {
    handleNodeClick (data) {
      console.log(data)
    }
  }
}
</script>

<style scoped>
.host-tree {
  border-right: solid 1px #e4e7ed;
  height: 100%;
  min-width: 250px;
  float: left;
}
</style>
