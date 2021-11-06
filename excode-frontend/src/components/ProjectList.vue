<template>
  <div class="project-list">
    <div class="toolbar">
      <h1 class="title">Projects</h1>
      <div class="tool-buttons">
        <el-input
          placeholder="Search..."
          v-model="searchText"
          size="mini"
          class="searchbox"
        >
          <template #suffix>
            <i class="el-icon-search el-input__icon" @click="refresh"></i>
          </template>
        </el-input>
        <el-button
          type="primary"
          style="display: inline-block"
          @click="create"
          size="mini"
        >
          <i class="el-icon-plus"></i>
        </el-button>
        <el-button @click="refresh" style="display: inline-block" size="mini">
          <i class="el-icon-refresh"></i>
        </el-button>
      </div>
    </div>
    <el-table :data="tableData" class="table">
      <el-table-column type="selection" width="40"> </el-table-column>
      <el-table-column label="Project Name" width="180">
        <template #default="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Last Modified" width="180">
        <template #default="scope">
          <span>{{ scope.row.modifiedTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Operations">
        <template #default="scope">
          <el-button
            type="text"
            size="mini"
            @click="handleEdit(scope.$index, scope.row)"
          >
            Edit
          </el-button>
          <el-button
            type="text"
            size="mini"
            @click="handleConfigure(scope.$index, scope.row)"
          >
            Configure
          </el-button>
          <el-popconfirm
            title="Are you sure to delete this?"
            @confirm="handleDelete(scope.$index, scope.row)"
          >
            <template #reference>
              <el-button type="text" size="mini"> Delete </el-button>
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
      :total="totalElements"
    >
    </el-pagination>

    <!-- Create Project Dialog -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      class="dialog"
      :before-close="handleClose"
    >
      <el-form ref="form" :model="form" label-width="auto">
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
      form: {}
    }
  },
  methods: {
    refresh () {
      const _this = this
      let searchParam = ''
      if (this.searchText !== '') {
        searchParam = '&search=' + this.searchText
      }
      axios
        .get(
          '/api/excode/project?pageSize=' +
            this.pageSize +
            '&pageNo=' +
            (this.pageNo - 1) +
            searchParam
        )
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
      axios
        .post('/api/excode/project', this.form)
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
      axios
        .delete('/api/excode/project/' + row.id)
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
        .then((_) => {
          done()
          this.dialogVisible = false
        })
        .catch((_) => {})
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

<style scoped>
.project-list {
  background-color: white;
  border: solid 1px #eee;
  border-radius: 2px;
  box-sizing: border-box;
  margin: 10px;
  padding: 10px;
}
.title {
  font-weight: 400;
  font-size: 18px;
}
.searchbox {
  height: 20px;
  width: 240px;
  margin-right: 10px;
}
.toolbar {
  height: 50px;
  margin-left: 10px;
  margin-right: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.table {
  width: calc(100% - 10px);
  margin-bottom: 10px;
}
:deep(.table .el-table__header-wrapper th) {
  background: #e3e3e3;
  height: 28px;
  font-size: 12px;
  font-weight: 100;
  color: #555;
  text-align: left;
  border-right: solid 1px white;
}
:deep(.table .el-table__row) {
  text-align: left;
  height: 34px;
  font-size: 12px;
  font-weight: 100;
  color: #555;
}
:deep(.table .el-table__row:nth-child(even)) {
  background: #f7f7f7;
}
:deep(.table .cell) {
  line-height: 16px;
}
:deep(.table .el-checkbox) {
  height: 16px;
}
.dialog {
  max-width: 500px !important;
}
</style>
