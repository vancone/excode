<template>
  <div id="app">
    <div class="top-bar">
      <h1 class="app-title">ExCRUD</h1>
    </div>
    <el-row class="tac">
      <el-col :span="3" style="height:100%;min-width:180px">
        <div class="left-pane">
          <el-menu
            style="background:transparent;border-right:none"
            default-active="projects"
            :router=true
            @open="handleOpen"
            @close="handleClose"
          >
            <el-menu-item index="/projects">
              <i class="el-icon-menu"></i>
              <span slot="title">Projects</span>
            </el-menu-item>
            <el-menu-item index="/hosts">
              <i class="el-icon-menu"></i>
              <span slot="title">Hosts</span>
            </el-menu-item>
            <!-- <el-menu-item index="3" disabled>
              <i class="el-icon-document"></i>
              <span slot="title">Documents</span>
            </el-menu-item> -->
            <el-menu-item index="/settings">
              <i class="el-icon-setting"></i>
              <span slot="title">Settings</span>
            </el-menu-item>
          </el-menu>
          <div style="position:fixed;left: 0px;bottom: 0px">
            <p class="copyright">v0.0.1</p>
          </div>
        </div>
      </el-col>
      <el-col :span="21" style="height:100%">
        <router-view />
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'App',
  methods: {
    handleOpen (key, keyPath) {
      console.log(key, keyPath)
    },
    handleClose (key, keyPath) {
      console.log(key, keyPath)
    }
  }
}
</script>

<style>
html,
body {
  margin: 0;
  height: 100%;
  width: 100%;
  background-color: #f7f7f7;
}
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  height: 100%;
  width: 100%;
  overflow: hidden;
}
.top-bar {
  background-color: white;
  height: 50px;
  width: 100%;
  text-align: left;
  border-bottom: solid 1px #e5e5e5;
  /* box-shadow: 0 0 5px rgba(0, 0, 0, 0.25); */
}
.app-title {
  margin: 0;
  font-size: 20px;
  line-height: 50px;
  margin-left: 20px;
}
.left-pane {
  border-right: solid 1px #e5e5e5;
  background: #f5f5f5;
  height: inherit;
  text-align: left;
}
.tac {
  height: calc(100% - 51px);
}
.copyright {
  font-size: 10px;
  margin-left: 5px;
  margin-top: 5px;
  margin-bottom: 5px;
  color: #aaa;
}
</style>
