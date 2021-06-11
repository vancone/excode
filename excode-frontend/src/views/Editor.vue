<template>
  <div class="editor">
    <div class="toolbar">
      <div class="button-save" @click="save"><img src="../assets/save.svg"/></div>
      <el-button type="primary" size="mini" class="button-export">Export</el-button>
    </div>
    <el-row style="height:calc(100% - 35px);">
      <el-col :span="19" style="height: 100%">
        <el-tabs tab-position="left" class="tabs">
          <el-tab-pane label="Spring Boot" style="height: 100%;">
            <SpringBootPanel/>
          </el-tab-pane>
          <el-tab-pane label="Vue.js">Config</el-tab-pane>
          <el-tab-pane label="Document">Role</el-tab-pane>
        </el-tabs>
      </el-col>

      <el-col :span="5" style="background: #fff; height: 100%;">
        <div style="height: 30px;width:100%; border-bottom: solid 1px #ddd;text-align: left;">
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
import axios from 'axios'
import ExportDialog from '@/components/ExportDialog.vue'
import SpringBootPanel from '@/components/SpringBootPanel.vue'
let id = 1000
export default {
  name: 'Editor',
  components: { ExportDialog, SpringBootPanel },
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
      }],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      project: {}
    }
  },
  methods: {
    save () {
      alert(JSON.stringify(this.project))
    },
    getUrlParam (name) {
      var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)')
      var r = window.location.search.substr(1).match(reg)
      if (r != null) return unescape(r[2])
      return null
    },
    load () {
      const _this = this
      axios.get('/api/excode/project/' + this.getUrlParam('id'))
        .then((res) => {
          _this.project = res.data.data
          localStorage.setItem('project', JSON.stringify(res.data.data))
        })
        .catch((err) => {
          console.log(err)
        })
    },
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
  },
  mounted: function () {
    this.load()
  }
}
</script>

<style scoped>
.editor {
  height: calc(100% - 55px);
  width: 100%;
  background: #f5f5f5;
}
.toolbar {
  height: 35px;
  width: 100%;
  background: white;
  border-bottom: solid 1px #ddd;
}
.button-save {
  height: 35px;
  width: 35px;
  cursor: pointer;
  margin-top: 0px;
  margin-left: 10px;
  float: left;
}
.button-save:hover {
  background: #eee;
}
.button-save img {
  margin-top: 7px;
  height: 20px;
}
.button-export {
  margin-top: 3px;
  margin-right: 5px;
  float: right;
}
.tabs {
  height: 100%;
  text-align: left;
}
/deep/ .el-tabs__content {
  height: 100%;
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
