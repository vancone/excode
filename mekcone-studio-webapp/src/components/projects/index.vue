<template>
  <div class="hello">
    <el-row :gutter="20">
      <el-col :span="19">
        <h2 class="projects-title">Projects</h2>
        <el-table :data="projectList" class="project-table"
          :header-cell-style="{background:'transparent'}"
          :row-style="{cursor:'pointer'}"
          @row-click="openProject">
          <!-- <el-table-column prop="id" label="ID" ></el-table-column> -->
          <el-table-column prop="name" label="Project name" ></el-table-column>
          <el-table-column prop="group" label="Group" width="200"></el-table-column>
          <el-table-column prop="modifiedTime" label="Modified time" width="200"></el-table-column>
        </el-table>
      </el-col>
      <el-col :span="5">
        <div style="text-align: left;padding-top: 40px;">
          <input ref="file" type="file" accept=".xml,.json" style="display:none" @change="importProject($event)">
          <el-button type="primary" style="height: 35px;width: 150px;padding-top:10px;margin-bottom:10px">New project</el-button><br>
          <el-button @click="$refs.file.click()" style="height: 35px;width: 150px;padding-top:10px">Import</el-button>
          <!-- <button class="button-create" @click="edit">New project</button> -->
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'InitialPage',
  data () {
    return {
      projectList: []
    }
  },
  methods: {
    edit () {
      // this.$router.push('/edit')
    },
    getList () {
      this.projectList = []
      this.axios.get('/api/studio/project').then(res => {
        if (res.data.code === 0) {
          res.data.data.map(item => {
            this.projectList.push({
              id: item.id,
              name: item.name,
              group: item.groupId,
              modifiedTime: item.modifiedTime
            })
          })
        } else {
          this.$notify.error({
            title: 'Error',
            message: 'Fetch list failed. Invalid response data from server.'
          })
        }
      }).catch(res => {
        this.$notify.error({
          title: 'Error',
          message: 'Fetch list failed'
        })
      })
    },
    importProject (event) {
      var file = event.target.files[0]
      var formData = new FormData()
      formData.append('file', file)
      var config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      this.axios.post('/api/excrud/project/import', formData, config).then(res => {
        if (res.data.code === 0) {
          this.$notify({
            title: 'Success',
            message: 'Import project successfully',
            type: 'success'
          })
          this.getList()
        } else {
          this.$notify.error({
            title: 'Error',
            message: 'Import project failed'
          })
        }
      }).catch(res => {
        this.$notify.error({
          title: 'Error',
          message: 'Upload file failed'
        })
      })
    },
    openProject (row) {
      this.$router.push('/projects/editor?id=' + row.id)
    }
  },
  mounted: function () {
    this.getList()
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1,
h2 {
  font-weight: normal;
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
.projects-title {
  text-align: left;
  font-size: 18px;
  margin-top: 40px;
  margin-left: 20px;
  margin-bottom: 20px;

}
.project-table {
  margin-left: 20px;
  width: calc(100% - 40px);
  background: none;
}
.button-create {
  background-color: #195f71;
  color: white;
  border-radius: 2px;
  height: 30px;
  width: 150px;
  outline: none;
  border: none;
  margin-top: 40px;
  cursor: pointer;
}

.el-table--fit{
        /* padding: 20px; */
}
.el-table, .el-table__expanded-cell {
    background-color: transparent;
}

.el-table tr {
    background-color: transparent!important;
    cursor: pointer;
}
.el-table--enable-row-transition .el-table__body td, .el-table .cell{
    background-color: transparent;
}
</style>
