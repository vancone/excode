<template>
  <div class="editor">
    <div class="toolbar">
      <el-button type="primary" size="mini" style="margin-top: 4px;float:right;">Export</el-button>
    </div>
    <el-row style="height:calc(100% - 35px);">
      <el-col :span="5" style="background:#fff;height:100%;">
        <div style="height:30px;width:100%;border-bottom: solid 1px #ddd;text-align:left;">
          <span style="line-height:30px;margin-left:10px;font-weight:bold">Data Object</span>
          <i class="el-icon-plus" style="float:right;margin-right:8px;margin-top:7px;cursor:pointer;"/>
        </div>
        <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick" default-expand-all :expand-on-click-node="false" class="tree">
          <template #default="{ node, data }">
        <span class="custom-tree-node">
          <span>{{ node.label }}</span>
          <span>
            <a @click="append(data)" style="margin-right:10px;"><i class="el-icon-plus"/></a>
            <a @click="remove(node, data)"><i class="el-icon-delete"/></a>
          </span>
        </span>
      </template>
        </el-tree>
      </el-col>
    </el-row>
    <ExportDialog/>
  </div>
</template>

<script>
import ExportDialog from '@/components/ExportDialog.vue'
let id = 1000
export default {
  name: 'Editor',
  components: { ExportDialog },
  data () {
    return {
      data: [{
        id: 1,
        label: 'Account',
        children: [{
          id: 11,
          label: 'username'
        }, {
          id: 12,
          label: 'password'
        }]
      }, {
        label: 'Article',
        children: [{
          label: 'title'
        }, {
          label: 'author'
        }, {
          label: 'content'
        }, {
          label: 'publishTime'
        }
        ]
      }, {
        label: 'Level one 3',
        children: [{
          label: 'Level two 3-1',
          children: [{
            label: 'Level three 3-1-1'
          }]
        }, {
          label: 'Level two 3-2',
          children: [{
            label: 'Level three 3-2-1'
          }]
        }]
      }],
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    }
  },
  methods: {
    append (data) {
      const newChild = { id: id++, label: 'testtest', children: [] }
      if (!data.children) {
        data.children = []
      }
      data.children.push(newChild)
      this.data = [...this.data]
    },

    remove (node, data) {
      const parent = node.parent
      const children = parent.data.children || parent.data
      const index = children.findIndex(d => d.id === data.id)
      children.splice(index, 1)
      this.data = [...this.data]
    },
    renderContent (h, { node, data, store }) {
      return h('span', {
        class: 'custom-tree-node'
      }, h('span', null, node.label), h('span', null, h('a', {
        onClick: () => this.append(data)
      }, 'Append '), h('a', {
        onClick: () => this.remove(node, data)
      }, 'Delete')))
    }
  }
}
</script>

<style scoped>
.editor {
  height: calc(100% - 55px);
  background: #f5f5f5;
}
.toolbar {
  height: 35px;
  width: 100%;
  background: white;
  border-bottom: solid 1px #ddd;
}
.tree {
  background: #fff;
}
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
</style>
