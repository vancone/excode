<template>
  <div class="spring-boot-panel">
    <el-form ref="form" :model="form" label-width="120px">
      <h1>POM Info</h1>
      <el-form-item label="Group ID">
        <el-input v-model="project.modules.spring_boot.groupId"></el-input>
      </el-form-item>
      <el-form-item label="Artifact ID">
        <el-input v-model="project.artifactId"></el-input>
      </el-form-item>
      <el-form-item label="Version">
        <el-input v-model="form.version"></el-input>
      </el-form-item>
      <el-form-item label="ORM Type">
        <el-select v-model="project.ormType" placeholder="please select ORM framework">
          <el-option label="MongoTemplate" value="mongo"></el-option>
          <el-option label="Spring Data JPA" value="jpa"></el-option>
          <el-option label="MyBatis - Annotation" value="mybatis-annotation"></el-option>
          <el-option label="MyBatis - XML" value="mybatis-xml"></el-option>
          <el-option label="MyBatis Plus" value="mybatis-plus"></el-option>
        </el-select>
      </el-form-item>
      <h1>Middleware</h1>
        <el-form-item label="MySQL">
          <el-input v-model="form.middleware.mysql.value"></el-input>
        </el-form-item>
      <h1>Extensions</h1>
      <el-form-item label="Lombok">
        <el-switch v-model="project.extensions.lombok.enable"></el-switch>
      </el-form-item>
      <el-form-item label="Swagger2">
        <el-switch v-model="project.extensions.swagger.enable"></el-switch>
      </el-form-item>
      <el-form-item label="Title">
        <el-input v-model="project.extensions.swagger.title"></el-input>
      </el-form-item>
      <el-form-item label="Jasypt">
        <el-switch v-model="project.extensions.jasypt.enable"></el-switch>
      </el-form-item>
      <el-form-item label="Jasypt Password">
        <el-input v-model="project.extensions.jasypt.password"></el-input>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'SpringBootPanel',
  data () {
    return {
      tableData: [],
      dialogVisible: false,
      dialogTitle: '',
      searchText: '',
      pageSize: 10,
      pageNo: 1,
      totalElements: 0,
      project: {
        modules: {
          spring_boot: {
            groupId: ''
          }
        },
        extensions: {
          lombok: {},
          swagger: {},
          jasypt: {}
        }
      },
      form: {
        groupId: 'com.vancone',
        artifactId: 'excode-demo',
        version: '1.0.0-SNAPSHOT',
        ormType: 'MyBatis - Annotation',
        middleware: {
          mysql: {
            name: 'MySQL',
            enable: true,
            value: 'mysql://root:******@127.0.0.1:3306/mydb'
          }
        },
        extensions: {
          lombok: {
            name: 'Lombok',
            enable: true
          },
          swagger: {
            name: 'Swagger2',
            enable: true,
            title: 'API Document'
          },
          jasypt: {
            name: 'Jasypt',
            enable: true,
            password: '************'
          }
        }
      }
    }
  },
  methods: {
    refresh () {
      this.project = localStorage.getItem('project')
    },
    create () {
      this.form = {}
      this.dialogTitle = 'Create Project'
      this.dialogVisible = true
    },
    save () {
      const _this = this
      axios.post('/api/excode/project', this.project)
        .then((res) => {
          _this.$message({
            message: 'Project saved.',
            type: 'success'
          })
          // _this.dialogVisible = false
          // _this.form = {}
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
    console.log('Project', this.project)
    // this.refresh()
    console.log('Loading project', localStorage.getItem('project'))
    this.project = localStorage.getItem('project')
  }
}
</script>

<style scoped>
.spring-boot-panel {
  padding-left: 20px;
  padding-top: 20px;
  padding-right: 20px;
  height: calc(100% - 20px);
  overflow: auto;
}
</style>
