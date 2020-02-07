<template>
  <div class="panelContainer">
    <div class="topBar">
      <h1>{{ appName }}</h1>
    </div>
    <el-menu background-color="#1f2d3d" text-color="white" router :default-active="$route.path">
      
      <el-menu-item index="/project" v-if="project==null">
        <i class="el-icon-s-grid"></i>
        <span>Project</span>
      </el-menu-item>

      <el-submenu index="1" v-if="project!=null">
        <template slot="title">
          <i class="el-icon-location"></i>
          <span>Project</span>
        </template>

          <el-menu-item index="/overview">
            <i class="el-icon-s-data"></i>
            <span>Overview</span>
          </el-menu-item>

          <el-menu-item index="/dependencies">
            <i class="el-icon-coin"></i>
            <span>Dependencies</span>
          </el-menu-item>

          <el-menu-item index="/datatables">
            <i class="el-icon-s-grid"></i>
            <span>Data Tables</span>
          </el-menu-item>


          <el-menu-item index="/configuration">
            <i class="el-icon-s-tools"></i>
            <span>Configuration</span>
          </el-menu-item>

          <el-menu-item index="/build">
            <i class="el-icon-s-cooperation"></i>
            <span>Build</span>
          </el-menu-item>
      </el-submenu>

      <el-submenu index="2">
        <template slot="title">
          <i class="el-icon-location"></i>
          <span>Host</span>
        </template>
        <!-- <router-link to="/overview">
          <el-menu-item index="2-1">
            <i class="el-icon-s-data"></i>
            <span>Overview</span>
          </el-menu-item>
        </router-link>
        <router-link to="/dependencies">
          <el-menu-item index="2-2">
            <i class="el-icon-coin"></i>
            <span>Dependencies</span>
          </el-menu-item>
        </router-link>
        <router-link to="/datatables">
          <el-menu-item index="2-3">
            <i class="el-icon-s-grid"></i>
            <span>Data Tables</span>
          </el-menu-item>
        </router-link>
        <router-link to="/configuration">
          <el-menu-item index="2-4">
            <i class="el-icon-s-tools"></i>
            <span>Configuration</span>
          </el-menu-item>
        </router-link>
        <router-link to="/build">
          <el-menu-item index="2-5">
            <i class="el-icon-s-cooperation"></i>
            <span>Build</span>
          </el-menu-item>
        </router-link> -->
      </el-submenu>
      <el-menu-item index="/settings">
        <i class="el-icon-s-grid"></i>
        <span>Settings</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: 'TopBar',
  props: [
    'project'
  ],
  data () {
    return {
      appName: 'AutoCRUD',
      items: [
        {text: 'Overview', link: '/overview', ico: ''},
        {text: 'Dependencies', link: '/dependencies'},
        {text: 'Data Tables', link: ''},
        {text: 'Properties', link: ''},
      ],
      activeIndex: "1"
    }
  },
  methods: {
    
  },
  mounted: function() {

    // switch(this.$route.name) {
    //   case 'Overview': this.activeIndex = 1; break;
    //   case 'Dependencies': this.activeIndex = 2; break;
    //   case 'Data Tables': this.activeIndex = 3; break;
    //   case 'Configuration': this.activeIndex = 4; break;
    //   case 'Build': this.activeIndex = 5; break;
    //   default: console.log(this.$route.name)
    // }
  }/* ,
  watch: {
    "project" (val, oldVal) {
      alert('')
    }
  } */
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.panelContainer {
  background: #1f2d3d;
  color: #bbbbbb;
  text-align: left;
  position: fixed;
  left: 0;
  top: 0;
  height: 100%;
  width: 200px;
  border-right: solid 1px white;
}
h1 {
  width: 100%;
  text-align: center;
  font-weight: normal;
  font-size: 24px;
  margin: 0;
  margin-top: 30px;
  display: inline-block;
  color: white;
}
p {
  width: calc(100% - 200px);
  text-align: center;
  margin: auto;
  display: inline-block;
  font-size: 13px;
}
.topBar {
  height: 95px;
  width: 100%;
  line-height: 30px;
  margin-bottom: 17px;
  cursor: default;
}
.tabBar {
  background: white;
  width: 100%;
}
.tabBar li {
  /* display: inline-block; */
  /* background: white; */
  height: 50px;
  width: calc(100% - 10px);
  padding-left: 10px;
  /* line-height: 36px; */
  cursor: pointer;
  width: auto;
  color: black;
  font-size: 30px;
  font-weight: 600;
  list-style-type: none;
}
.tabBar img {
  line-height: 50px;
  height: 22px;
  width: 22px;
  vertical-align: middle;
  margin-right: 10px;
  /* margin-top: 10px; */
  display: inline-block;
}
.tabBar li:hover {
  background: #f7f9fa;
}
.tabBar li p {
  display: inline-block;
  width: calc(100% - 35px);
  border-bottom: solid 1px #ddd;
  height: 50px;
  text-align: left;
  font-size: 16px;
  font-weight: normal;
  line-height: 50px;
}
.tabBar li button {
  display: inline-block;
}
a {
  color: black;
}
</style>
