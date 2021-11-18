<template>
  <!-- Data table header -->
  <div class="data-table-panel-header">
    <span style="">Git Repo</span>
    <i class="el-icon-plus" @click="openDataTableDialog" />
  </div>
  <!-- Data table tree -->
  <el-tree
    :data="project.dataTables"
    :props="defaultProps"
    @node-click="handleNodeClick"
    default-expand-all
    :expand-on-click-node="false"
    class="tree"
  >
    <template #default="{ node, data }">
      <span class="custom-tree-node">
        <span @dblclick="editDataTableKey(data)">
          <img
            src="./../assets/table.svg"
            v-if="data.root == true"
            class="data-table-key-icon"
          />
          <img
            src="./../assets/key.svg"
            v-if="data.root != true"
            class="data-table-key-icon"
          />
          {{ node.label }}
          <span
            style="color: #aaa"
            v-text="
              data.root === true
                ? data.dataSource.type + ':' + data.dataSource.database
                : data.type
            "
          ></span>
        </span>
        <span>
          <a @click="append(data)" style="margin-right: 10px"
            ><i class="el-icon-plus"
          /></a>
          <a @click="remove(node, data)"><i class="el-icon-delete" /></a>
        </span>
      </span>
    </template>
  </el-tree>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Editor',
  data () {
    return {
      dataTableDialogVisible: false,
      dataSourceDialogVisible: false,
      dataTableKeyDialogVisible: false,
      dataTableKeySelectDataSourceDialogVisible: false,
      defaultProps: {
        children: 'nodes',
        label: 'value'
      },
      dataObject: {
        value: '',
        type: 'MYSQL',
        name: ''
      },
      dataSourceOptions: [],
      dataTableKey: {},
      project: {
        modules: {
          spring_boot: {}
        },
        data: []
      }
    }
  },
  methods: {
    save () {
      alert(JSON.stringify(this.project))
    },
    back () {
      this.$router.push('/')
    },
    getUrlParam (name) {
      var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)')
      var r = window.location.search.substr(1).match(reg)
      if (r != null) return unescape(r[2])
      return null
    },
    toggleDataSourceType () {
      console.log('toggle data source type')
      const _this = this
      axios.get('/api/excode/data-source?type=' + this.dataObject.type)
        .then((res) => {
          _this.dataSourceOptions = res.data.data.content
          _this.dataObject.name = ''
        })
        .catch((err) => {
          console.log(err)
        })
    },
    openDataTableDialog () {
      this.toggleDataSourceType()
      this.dataTableDialogVisible = true
    },
    openDataSourceDialog () {
      this.dataSourceDialogVisible = true
    },
    openSelectDataSourceDialog () {
      this.dataTableKeySelectDataSourceDialogVisible = true
    },
    editDataTableKey (data) {
      this.dataTableKey = data
      if (this.dataTableKey.root === true) {
        this.dataTableKey.type = this.dataTableKey.dataSource.type
        if (this.dataTableKey.type === 'MYSQL') {
          this.dataTableKey.source = 'mysql://' + this.dataTableKey.dataSource.host + ':' + this.dataTableKey.dataSource.port + '/' + this.dataTableKey.dataSource.database + '?username=' + this.dataTableKey.dataSource.username
        }
      }
      this.dataTableKeyDialogVisible = true
    },
    load () {
      // const _this = this
      // axios.get('/api/excode/project/' + this.getUrlParam('id'))
      //   .then((res) => {
      //     _this.project = res.data.data
      //     localStorage.setItem('project', JSON.stringify(res.data.data))
      //   })
      //   .catch((err) => {
      //     console.log(err)
      //   })
    },
    append (data) {
      const newChild = { label: 'testtest', children: [] }
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
    // this.load()
  }
}
</script>

<style scoped>
.editor {
  height: calc(100% - 50px);
  width: 100%;
  background: #f5f5f5;
}
.toolbar {
  height: 35px;
  width: 100%;
  background: white;
  border-bottom: solid 1px #ddd;
}
.tabs {
  height: 100%;
  text-align: left;
}
:deep(.el-tabs__content) {
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
:deep(.el-tree-node:focus>.el-tree-node__content) {
  background-color: #0074e8;
  color: white;
}
.data-table-panel {
  background: #fff;
  height: 100%;
  text-align: left;
  border-left: solid 1px #ddd;
}
.data-table-panel-header {
  height: 30px;
  width:100%;
  border-bottom: solid 1px #ddd;
  text-align: left;
  background: #f9f9fa;
}
.data-table-panel-header span {
  line-height: 30px;
  margin-left: 10px;
  font-weight: 400;
  font-size: 14px;
}
.data-table-panel-header i {
  float: right;
  margin-right: 8px;
  margin-top: 7px;
  cursor: pointer;
}
.data-table-key-icon {
  height: 14px;
  width: 14px;
  vertical-align: middle;
  margin-right: 3px;
}
:deep(.el-tabs--left .el-tabs__item.is-left) {
  text-align: left;
}
</style>
