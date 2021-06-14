<template>
  <div class="editor">
    <div class="toolbar">
      <div class="button-save" @click="back"><img src="../assets/back.svg"/></div>
      <div class="button-save" @click="save"><img src="../assets/save.svg"/></div>
      <div class="button-save" @click="openDataSourceDialog"><img src="../assets/database.svg"/></div>
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

      <!-- Data table -->
      <el-col :span="5" style="background: #fff; height: 100%;border-left:solid 1px #ddd;">
        <div style="height: 30px;width:100%; border-bottom: solid 1px #ddd;text-align: left;background:#f9f9fa;">
          <span style="line-height:30px;margin-left:10px;font-weight:400;font-size:14px;">Data Object</span>
          <i class="el-icon-plus" @click="openDataObjectDialog" style="float:right;margin-right:8px;margin-top:7px;cursor:pointer;"/>
        </div>
        <el-tree :data="project.data" :props="defaultProps" @node-click="handleNodeClick" default-expand-all :expand-on-click-node="false" class="tree">
          <template #default="{ node, data }">
            <span class="custom-tree-node">
              <span @dblclick="editDataTableKey(data)"><img src="../assets/key.svg" style="height:14px;width:14px;vertical-align:middle;margin-right:5px;"/>{{ node.label }} <span style="color:#aaa;" v-text="data.root===true? data.dataSource.type + ':' + data.dataSource.database: data.type"></span></span>
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
    <el-dialog title="Data Object" v-model="dataObjectDialogVisible" width="50%" :before-close="handleClose">
      <el-form ref="form" :model="dataObject" label-width="120px">
        <el-form-item label="Table Name">
          <el-input v-model="dataObject.value"></el-input>
        </el-form-item>
        <el-form-item label="Data Source Type">
          <el-select v-model="dataObject.type" placeholder="please select ORM framework">
            <el-option label="Elasticsearch" value="elasticsearch"></el-option>
            <el-option label="MariaDB / MySQL" value="mysql"></el-option>
            <el-option label="MongoDB" value="mongodb"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="save">Save</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- Data source dialog -->
    <el-dialog title="Data Source" v-model="dataSourceDialogVisible" width="70%" :before-close="handleClose">
      <data-source-panel/>
    </el-dialog>

    <!-- Data table key dialog -->
    <el-dialog title="Data Table Key" v-model="dataTableKeyDialogVisible" width="40%" :before-close="handleClose">
      <el-form ref="dataTableKey" :model="dataObject" label-width="80px" style="text-align:left;">
        <el-form-item label="Value">
          <el-input v-model="dataTableKey.value"></el-input>
        </el-form-item>
        <el-form-item label="Type">
          <el-select v-model="dataTableKey.type" placeholder="">
            <el-option label="Elasticsearch" value="elasticsearch"></el-option>
            <el-option label="MariaDB / MySQL" value="mysql"></el-option>
            <el-option label="MongoDB" value="mongodb"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Data Source" v-if="dataTableKey.root">
          <el-select v-model="dataTableKey.type" placeholder="">
            <el-option label="Elasticsearch" value="elasticsearch"></el-option>
            <el-option label="MariaDB / MySQL" value="mysql"></el-option>
            <el-option label="MongoDB" value="mongodb"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dataTableKeyDialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="saveDataTableKey">Save</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
import DataSourcePanel from '@/components/DataSourcePanel'
import ExportDialog from '@/components/ExportDialog.vue'
import SpringBootPanel from '@/components/SpringBootPanel.vue'
export default {
  name: 'Editor',
  components: { DataSourcePanel, ExportDialog, SpringBootPanel },
  data () {
    return {
      dataObjectDialogVisible: false,
      dataSourceDialogVisible: false,
      dataTableKeyDialogVisible: false,
      defaultProps: {
        children: 'nodes',
        label: 'value'
      },
      dataObject: {
        value: '',
        type: 'mysql'
      },
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
    openDataObjectDialog () {
      this.dataObjectDialogVisible = true
    },
    openDataSourceDialog () {
      this.dataSourceDialogVisible = true
    },
    editDataTableKey (data) {
      this.dataTableKey = data
      if (this.dataTableKey.root === true) {
        this.dataTableKey.type = this.dataTableKey.dataSource.type
      }
      this.dataTableKeyDialogVisible = true
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
  /* margin-left: 10px; */
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
/deep/ .el-tree-node__content:hover {}

/deep/ .el-tree-node:focus>.el-tree-node__content{
  background-color: #0074e8;
  color: white;
}
</style>
