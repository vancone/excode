<template>
  <div class="hello">
    <div class="toolbar">
      <h1 style="float:left;">Projects</h1>
      <div class="tool-buttons" style="float:right;">
        <el-input placeholder="Search..." v-model="searchText" style="display:inline-block;width:300px;margin-right:10px;">
          <template #suffix>
            <i class="el-icon-search el-input__icon" @click="refresh"></i>
          </template>
        </el-input>
        <el-button type="primary" style="display:inline-block;" @click="create"><i class="el-icon-plus" style="margin-right:10px;"></i>Create</el-button>
        <el-button @click="refresh" style="display:inline-block;"><i class="el-icon-refresh" style="margin-right:10px;"></i>Refresh</el-button>
      </div>
    </div>
    <el-table :data="tableData" style="width:calc(100% - 40px);margin:20px">
      <el-table-column label="Project Name" width="180">
        <template #default="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Name" width="180">
        <template #default="scope">
          <el-popover effect="light" trigger="hover" placement="top">
            <template #reference>
              <div class="name-wrapper">
                <el-tag size="medium">{{ scope.row.name }}</el-tag>
              </div>
            </template>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="Last Modified" width="180">
        <template #default="scope">
          <span>{{ scope.row.modifiedTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Operations">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.$index, scope.row)"><i class="el-icon-edit"></i>&nbsp;Edit</el-button>
          <el-button size="mini" @click="handleConfigure(scope.$index, scope.row)"><i class="el-icon-setting"></i>&nbsp;Configure</el-button>
          <el-popconfirm title="Are you sure to delete this?" @confirm="handleDelete(scope.$index, scope.row)">
            <template #reference>
              <el-button size="mini" type="danger"><i class="el-icon-delete"></i>&nbsp;Delete</el-button>
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

    <!-- Create Project Dialog -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="50%" :before-close="handleClose">
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item label="Project name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="save">Save</el-button>
        </span>
      </template>
    </el-dialog>
    <div class="footer">
      <p>ExCode Low-Code Development Platform</p>
      <p>&copy; 2020-2021 VanCone. All rights reserved.</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'ProjectList',
  data () {
    return {
      tableData: [],
      dialogVisible: false,
      dialogTitle: '',
      searchText: '',
      pageSize: 10,
      pageNo: 1,
      totalElements: 0,
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      }
    }
  },
  methods: {
    refresh () {
      const _this = this
      let searchParam = ''
      if (this.searchText !== '') {
        searchParam = '&search=' + this.searchText
      }
      axios.get('/api/excode/project?pageSize=' + this.pageSize + '&pageNo=' + (this.pageNo - 1) + searchParam)
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
      this.dialogTitle = 'Create Project'
      this.dialogVisible = true
    },
    save () {
      const _this = this
      axios.post('/api/excode/project', this.form)
        .then((res) => {
          _this.$message({
            message: 'Project saved.',
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
      this.$router.push('/editor?id=' + row.id)
    },
    handleConfigure (index, row) {
      console.log(index, row)
      this.form = row
      this.dialogTitle = 'Configure Project'
      this.dialogVisible = true
    },
    handleDelete (index, row) {
      const _this = this
      axios.delete('/api/excode/project/' + row.id)
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
    }
  },
  mounted: function () {
    this.refresh()
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
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
a {
  color: #42b983;
}
.toolbar {
  margin-top: 20px;
  margin-left: 20px;
  margin-right: 20px;
  text-align: left;
}
.footer {
  color: #999;
  position: absolute;
  font-size: 14px;
  bottom: 0;
  width: 100%;
}
.footer p {
  margin: 5px;
}
</style>
