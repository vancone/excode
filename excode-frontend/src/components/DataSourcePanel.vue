<template>
  <div class="hello">
    <div class="toolbar">
      <div class="tool-buttons" style="float:right;">
        <el-input placeholder="Search..." v-model="searchText" style="display:inline-block;width:300px;margin-right:10px;">
          <template #suffix>
            <i class="el-icon-search el-input__icon" @click="refresh"></i>
          </template>
        </el-input>
        <el-button type="primary" style="display:inline-block;" @click="create"><i class="el-icon-plus"></i></el-button>
        <el-button @click="refresh" style="display:inline-block;"><i class="el-icon-refresh"></i></el-button>
      </div>
    </div>
    <el-table :data="tableData" style="width:calc(100% - 40px);margin:20px">
      <el-table-column label="Name">
        <template #default="scope">
          <span class="data-source-name" @click="handleEdit(scope.$index, scope.row)">{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Type">
        <template #default="scope">
          <el-tag size="medium">{{ scope.row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Host">
        <template #default="scope">
          <span>{{ scope.row.host }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Port">
        <template #default="scope">
          <span>{{ scope.row.port }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Username">
        <template #default="scope">
          <span>{{ scope.row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Last Modified">
        <template #default="scope">
          <span>{{ scope.row.modifiedTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Operations">
        <template #default="scope">
          <!-- <el-button size="mini" @click="handleEdit(scope.$index, scope.row)"><i class="el-icon-edit"></i></el-button> -->
          <el-popconfirm title="Are you sure to delete this?" @confirm="handleDelete(scope.$index, scope.row)">
            <template #reference>
              <el-button size="mini" type="danger"><i class="el-icon-delete"></i></el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pagition -->
    <el-pagination
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      v-model:currentPage="pageNo"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="10"
      layout="total, sizes, prev, pager, next"
      :total="totalElements">
    </el-pagination>

    <!-- Create Data source dialog -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="50%" :before-close="handleClose">
      <el-form ref="form" :model="form" label-width="80px" style="text-align:left;">
        <el-form-item label="Name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="Type" style="text-align:left;">
          <el-select v-model="form.type" placeholder="Please select data source type">
            <el-option label="Elasticsearch" value="ELASTICSEARCH"></el-option>
            <el-option label="MariaDB / MySQL" value="MYSQL"></el-option>
            <el-option label="MongoDB" value="MONGODB"></el-option>
            <el-option label="Redis" value="REDIS"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Host">
          <el-input v-model="form.host"></el-input>
        </el-form-item>
        <el-form-item label="Port">
          <el-input-number :min="1" :max="65535" v-model="form.port"></el-input-number>
        </el-form-item>
        <el-form-item label="Database" v-if="form.type !== 'REDIS'">
          <el-input v-model="form.database"></el-input>
        </el-form-item>
        <el-form-item label="Username" v-if="form.type !== 'REDIS'">
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="Password">
          <el-input v-model="form.password" show-password></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button style="float:left" @click="testConnection">{{ testButtonText }}</el-button>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="save">Save</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'DataSourcePanel',
  data () {
    return {
      tableData: [],
      dialogVisible: false,
      dialogTitle: '',
      searchText: '',
      pageSize: 10,
      pageNo: 1,
      totalElements: 0,
      form: {},
      testButtonText: 'Test Connection'
    }
  },
  methods: {
    refresh () {
      const _this = this
      let searchParam = ''
      if (this.searchText !== '') {
        searchParam = '&search=' + this.searchText
      }
      axios.get('/api/excode/data-source?pageSize=' + this.pageSize + '&pageNo=' + (this.pageNo - 1) + searchParam)
        .then((res) => {
          _this.tableData = res.data.data.content
          _this.totalElements = res.data.data.totalElements
        })
        .catch((err) => {
          console.log(err)
        })
    },
    create () {
      this.form = {}
      this.dialogTitle = 'Create Data Source'
      this.dialogVisible = true
    },
    save () {
      const _this = this
      axios.post('/api/excode/data-source', this.form)
        .then((res) => {
          _this.$message({
            message: 'Data source saved.',
            type: 'success'
          })
          _this.dialogVisible = false
          _this.form = {}
          _this.refresh()
        })
        .catch((err) => {
          console.log(err)
        })
    },
    handleEdit (index, row) {
      console.log(index, row)
      this.form = row
      this.dialogTitle = 'Edit Data Source'
      this.dialogVisible = true
    },
    handleDelete (index, row) {
      const _this = this
      axios.delete('/api/excode/data-source/' + row.id)
        .then((res) => {
          _this.$message('Operation Success')
          _this.refresh()
        })
        .catch((err) => {
          console.log(err)
        })
    },
    handleClose (done) {
      this.$confirm('Are you sure to close this dialog?')
        .then(_ => {
          done()
          this.dialogVisible = false
        })
        .catch(_ => {})
    },
    handleSizeChange (val) {
      console.log(`${val} items per page`)
      this.pageSize = val
      this.refresh()
    },
    handleCurrentChange (val) {
      console.log(`current page: ${val}`)
      this.pageNo = val
      this.refresh()
    },
    testConnection () {
      const _this = this
      this.testButtonText = 'Testing...'
      axios.get('/api/excode/data-source/test/' + this.form.id)
        .then((res) => {
          if (res.data.code === 0) {
            _this.$message({
              message: res.data.message,
              type: 'success'
            })
          } else {
            _this.$message({
              message: res.data.message,
              type: 'error'
            })
          }
          _this.testButtonText = 'Test Connection'
        })
        .catch((err) => {
          console.log(err)
          _this.testButtonText = 'Test Connection'
        })
    }
  },
  mounted: function () {
    this.refresh()
  }
}
</script>

<style scoped>
.toolbar {
  margin-top: 20px;
  margin-left: 20px;
  margin-right: 20px;
  text-align: left;
}
.data-source-name {
  cursor: pointer;
}
.data-source-name:hover {
  text-decoration: underline;
}
/deep/ .el-dialog__body {
  padding-top: 10px;
}
</style>
