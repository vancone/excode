<template>
  <div class="panelContainer">
    <h1>{{ appName }}</h1>
    <el-menu background-color="#1f2d3d" text-color="white" router :default-active="$route.path">
      ## menuItems ##
    </el-menu>
  </div>
</template>

<script>
export default {
  name: 'TopBar',
  props: [
    'appName'
  ],
  data () {
    return {
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
  text-align: left;
  font-weight: normal;
  font-size: 22px;
  margin: 0;
  margin-left: 20px;
  margin-top: 30px;
  margin-bottom: 50px;
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
/* .topBar {
  height: 95px;
  width: 100%;
  line-height: 30px;
  margin-bottom: 17px;
  cursor: default;
} */
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
