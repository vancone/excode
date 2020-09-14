<template>
  <div class="editor-container">
    <div class="toolbar">
      <el-button type="primary" size="mini" class="button-export">Export</el-button>
    </div>
    <el-row style="height:100%">
      <el-col :span="4" style="height:100%;min-width:180px;max-width:240px;">
        <div class="second-left-pane">
          <el-menu
            style="background:transparent;border-right:none;width:100%"
            default-active="projects"
            :unique-opened=true
            :router=true
            @open="handleOpen"
            @close="handleClose"
          >
            <!-- <el-menu-item class="module-item" v-for="item in project.moduleSet" :key="project.moduleSet.id">
              <i class="el-icon-menu"></i>
              <span slot="title">mod::{{item.type}}</span>
            </el-menu-item>-->
            <el-submenu
              class="module-item"
              v-for="item in project.moduleSet"
              :key="item.type"
              :index="item.type"
            >
            <template slot="title">
              <i class="el-icon-cpu"></i>
              <span slot="title">{{item.type}}</span>
            </template>
            <el-menu-item-group style="margin-left:10px">
              <template slot="title">Advanced</template>
              <el-menu-item class="module-sub-item" index="/projects/editor/mod-sb-ext">
                <!-- <i class="el-icon-cpu"></i> -->
                <span slot="title">Extensions</span>
              </el-menu-item>
              <!-- <el-menu-item class="module-sub-item" v-for="subItem in item.extensions" :key="subItem.id" index="/projects/editor/spring-boot">
                <i class="el-icon-cpu"></i>
                <span slot="title">{{subItem.id}}</span>
              </el-menu-item> -->
            </el-menu-item-group>
            </el-submenu>
          </el-menu>
        </div>
      </el-col>
      <el-col :span="20">
        <router-view></router-view>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'EditorPage',
  data () {
    return {
      project: {},
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    }
  },
  methods: {
    handleOpen (key, keyPath) {
      console.log(key, keyPath)
      for (var item in this.$utils.project.moduleSet) {
        if (this.$utils.project.moduleSet[item].type === key) {
          this.$utils.extensions = this.$utils.project.moduleSet[item].extensions
          break
        }
      }
    },
    handleClose (key, keyPath) {
      console.log(key, keyPath)
    },
    handleNodeClick (data) {
      console.log(data)
    },
    loadProject () {
      this.axios
        .get('/api/studio/project/' + this.$utils.getUrlKey('id'))
        .then(res => {
          if (res.data.code === 0) {
            this.$notify({
              title: 'Success',
              message: 'Import project successfully',
              type: 'success'
            })
            this.$utils.project = res.data.data
            this.project = this.$utils.project
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
    }
  },
  mounted: function () {
    this.loadProject()
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1,
h2 {
  font-weight: normal;
}
.editor-container {
  height: 100%;
}
.toolbar {
  width: 100%;
  height: 40px;
  background: #f4f4f4;
  border-bottom: solid 1px #e5e5e5;
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
  width: calc(100% - 140px);
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
.second-left-pane {
  border-right: solid 1px #e5e5e5;
  background: #f4f4f4;
  height: 100%;
  text-align: left;
  display: inline-block;
  width: 100%;
  overflow-y: auto;
}
.module-item {
  /* height: 40px; */
  width: calc(100% + 10px);
  margin-left: -10px;
  font-size: 14px;
}
.module-item /deep/ .el-submenu__title {
  height: 40px;
  line-height: 40px;
}
.module-sub-item {
  height: 40px;
  width: calc(100% + 10px);
  /* margin-left: -10px; */
  line-height: 40px;
}
.button-export {
  margin-top: 5px;
  margin-right: 10px;
}
</style>
