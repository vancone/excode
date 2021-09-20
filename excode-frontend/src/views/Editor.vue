<template>
  <div class="editor">
    <div class="toolbar">
      <div class="button button-back" @click="back"></div>
      <div class="button button-save" @click="save">
        <span>Save</span>
      </div>
      <div class="button button-datasource" @click="openDataSourceDialog">
        <span>Data Source</span>
      </div>
      <div class="button button-export" @click="openDataSourceDialog">
        <span>Export</span>
      </div>
    </div>
    <el-row style="height:calc(100% - 35px);">
      <el-col :span="19" style="height: 100%">
        <el-tabs tab-position="left" class="tabs">
          <el-tab-pane label="Spring Boot" style="height: 100%;">
            <!-- <SpringBootPanel/> -->
          </el-tab-pane>
          <el-tab-pane label="Vue.js">Vue.js</el-tab-pane>
          <el-tab-pane label="Document">Document</el-tab-pane>
        </el-tabs>
      </el-col>

      <!-- Data table -->
      <el-col :span="5" class="data-table-panel">
        <!-- Data table header -->
        <div class="data-table-panel-header">
          <span style="">Data Table</span>
          <i class="el-icon-plus" @click="openDataTableDialog"/>
        </div>
        <!-- Data table tree -->
        <el-tree :data="project.dataTables" :props="defaultProps" @node-click="handleNodeClick" default-expand-all :expand-on-click-node="false" class="tree">
          <template #default="{ node, data }">
            <span class="custom-tree-node">
              <span @dblclick="editDataTableKey(data)">
                <img src="@/assets/table.svg" v-if="data.root == true" class="data-table-key-icon" />
                <img src="@/assets/key.svg" v-if="data.root != true" class="data-table-key-icon" />
                {{ node.label }} <span style="color:#aaa;" v-text="data.root===true? data.dataSource.type + ':' + data.dataSource.database: data.type"></span>
              </span>
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

    <!-- Create data table dialog -->
    <el-dialog title="Data Table" v-model="dataTableDialogVisible" width="50%" :before-close="handleClose">
      <el-form ref="form" :model="dataObject" label-width="140px" style="text-align:left;">
        <el-form-item label="Table Name">
          <el-input v-model="dataObject.value" maxlength="20" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="Data Source Type">
          <el-select v-model="dataObject.type" @change="toggleDataSourceType" style="width:100%">
            <el-option label="Elasticsearch" value="ELASTICSEARCH"></el-option>
            <el-option label="MariaDB / MySQL" value="MYSQL"></el-option>
            <el-option label="MongoDB" value="MONGODB"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Data Source">
          <el-select v-model="dataObject.name" style="width:100%">
            <el-option v-for="item in dataSourceOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dataTableDialogVisible = false">Cancel</el-button>
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
          <el-select v-model="dataTableKey.type" placeholder="" @change="toggleDataSourceType">
            <el-option label="Elasticsearch" value="ELASTICSEARCH"></el-option>
            <el-option label="MariaDB / MySQL" value="MYSQL"></el-option>
            <el-option label="MongoDB" value="MONGODB"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Source" v-if="dataTableKey.root">
          <span v-text="dataTableKey.source"></span>
          <el-button type="primary" style="float:right;" size="small" @click="openSelectDataSourceDialog">Select</el-button>
        </el-form-item>
      </el-form>
      <el-dialog title="Select..." v-model="dataTableKeySelectDataSourceDialogVisible">
        <data-source-panel />
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dataTableKeySelectDataSourceDialogVisible = false">Cancel</el-button>
            <el-button type="primary" @click="saveDataTableKey">OK</el-button>
          </span>
        </template>
      </el-dialog>
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
import ExportDialog from '@/components/ExportDialog'
// import SpringBootPanel from '@/components/SpringBootPanel.vue'
export default {
  name: 'Editor',
  components: { DataSourcePanel, ExportDialog/* , SpringBootPanel */ },
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
.button {
  height: 35px;
  width: auto;
  cursor: pointer;
  margin-top: 0px;
  padding: 0 8px 0 8px;
  /* margin-left: 10px; */
  float: left;
  background-size: 16px 16px;
  background-repeat: no-repeat;
  background-position: 10px center;
  padding-left: 32px;
}
.button:hover {
  background-color: #eee;
  color: #8ba74f;
}
.button span {
  color: #999;
  font-size: 12px;
  line-height: 35px;
  vertical-align: middle;
  margin: 0;
}
.button-back {
  background-image: url(../assets/back.svg);
}
.button-save {
  background-image: url(../assets/save.svg);
}
.button-datasource {
  background-image: url(../assets/database.svg);
}
.button-export {
  background-image: url(../assets/export.svg);
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
  height: 30px;width:100%;
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
</style>
