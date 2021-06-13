<template>
  <div class="hello">
    <!-- Create Project Dialog -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="50%" :before-close="handleClose">
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item label="Project name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item>
          <el-checkbox label="Spring Boot" name="type"></el-checkbox>
          <el-checkbox label="Vue.js" name="type"></el-checkbox>
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
  name: 'ExportDialog',
  props: {
    msg: String
  },
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
