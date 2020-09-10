<template>
  <div class="hello">
    <el-row :gutter="20">
      <el-col :span="19" style>
        <h2 class="projects-title">Settings</h2>
        <div class="card-container">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>About</span>
            </div>
            <div class="text item" style="font-size:14px">
              <p>MekCone Studio (v0.0.1)</p>
              <p>&copy; 2020 MekCone. All rights reserved.</p>
              <el-link href="https://studio.mekcone.com/tos" target="_blank" type="primary">Terms of service</el-link>
            </div>
          </el-card>
        </div>
      </el-col>
      <el-col :span="5">
        <!-- <div style="text-align: left;padding-top: 40px;">
          <input
            ref="file"
            type="file"
            accept=".xml, .json"
            style="display:none"
            @change="importProject($event)"
          />
          <el-button
            type="primary"
            style="height: 35px;width: 150px;padding-top:10px;margin-bottom:10px"
          >New Host</el-button>
          <br />
          <el-button
            @click="$refs.file.click()"
            style="height: 35px;width: 150px;padding-top:10px"
          >Import</el-button>
        </div> -->
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
      this.axios
        .get('/api/excrud/project')
        .then(res => {
          if (res.data.code === 0) {
            res.data.data.map(item => {
              this.projectList.push({
                id: item.id,
                name: item.name.defaultValue,
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
        })
        .catch(res => {
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
      this.axios
        .post('/api/excrud/project/import', formData, config)
        .then(res => {
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
        })
        .catch(res => {
          this.$notify.error({
            title: 'Error',
            message: 'Upload file failed'
          })
        })
    },
    openProject (row) {
      this.$router.push('/edit?id=' + row.id)
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
.projects-title {
  text-align: left;
  font-size: 18px;
  margin-top: 40px;
  margin-left: 20px;
  margin-bottom: 20px;
}
.box-card {
  margin-left: 20px;
  min-width: 200px;
  width: 100%;
  box-shadow: none;
  display: inline-block;
  text-align: left;
  min-height: 150px;
  margin-bottom: 15px;
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
.card-container {
  text-align: left;
  padding-right: 20px;
}
</style>
