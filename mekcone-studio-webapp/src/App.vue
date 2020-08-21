<template>
  <div id="app">
    <div class="top-bar">
      <img src="./assets/png/logo.png" class="app-logo" />
      <h1 class="app-title">CRUD</h1>
    </div>
    <div class="left-pane">
      <el-menu
        style="background:transparent;border-right:none"
        default-active="projects"
        :router="true"
        @open="handleOpen"
        @close="handleClose"
      >
        <el-menu-item index="/projects" class="menu-item">
          <icon name="project" class="menu-icon" />
        </el-menu-item>
        <el-menu-item index="/hosts" class="menu-item">
          <icon name="server" class="menu-icon" />
        </el-menu-item>
        <!-- <el-menu-item index="3" disabled>
              <i class="el-icon-document"></i>
              <span slot="title">Documents</span>
        </el-menu-item>-->
        <el-menu-item index="/settings" class="menu-item">
          <i class="el-icon-setting" style="margin-left:-7px"></i>
        </el-menu-item>
      </el-menu>
    </div>
    <div class="container"><router-view style="height:100%;" /></div>
  </div>
</template>

<script>
export default {
  name: 'App',
  data () {
    return {
      // leftPaneWidth: 50
    }
  },
  methods: {
    handleOpen (key, keyPath) {
      console.log(key, keyPath)
    },
    handleClose (key, keyPath) {
      console.log(key, keyPath)
    }
  },
  watch: {
    leftPaneWidth: function (val, oldVal) {}
  }
}
</script>

<style>
html,body {
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
  text-align: right;
}
.top-bar {
  background-color: white;
  height: 50px;
  width: 100%;
  text-align: left;
  border-bottom: solid 1px #e5e5e5;
  z-index: 10000000;
}
.app-logo {
  height: 30px;
  width: 30px;
  margin-left: 10px;
  margin-top: 10px;
  display: inline-block;
}
.app-title {
  margin: 0;
  font-size: 20px;
  line-height: 50px;
  display: inline-block;
  font-weight: 400;
  vertical-align: top;
  color: #0499fd;
}
.left-pane {
  border-right: solid 1px #e5e5e5;
  background: #f4f4f4;
  height: inherit;
  text-align: left;
  overflow: hidden;
  display: inline-block;
  position: fixed;
  left: 0;
  top: 50px;
  width: 50px;
  z-index: 100;
}
.container {
  display: inline-block;
  height: 100%;
  width: calc(100% - 50px);
}
.menu-item {
  width: 50px;
  height:50px;
  padding:0;
  margin: 0;
}
.menu-icon {
  margin:0;
  padding:0;
  height: 22px;
  width: 22px;
  margin-left: -5px;
  margin-top: -8px;
  color: #999;
}
.menu-icon:hover {
  color: #195f81;
}
</style>
