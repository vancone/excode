<template>
  <div class="selectListContainer">
    <div class="outer_triangle"></div>
    <div class="inner_triangle"></div>
    <li>Add a dependency</li>
  </div>
</template>

<script>
export default {
  name: 'TopBar',
  data () {
    return {
      appName: 'AutoCRUD',
      tabs: [
        {text: 'Overview', link: '/overview', ico: ''},
        {text: 'Dependencies', link: '/dependencies'},
        {text: 'Data Tables', link: ''},
        {text: 'Properties', link: ''},
      ]
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.selectListContainer {
  background: white;
  color: #bbbbbb;
  text-align: left;
  position: fixed;
  right: 5px;
  top: 55px;
  height: 50px;
  width: 220px;
  border: solid 1px #eee;
  border-radius: 5px;
  /* overflow: hidden; */
}
 .outer_triangle {
  width: 0;
  height: 0;
  border: 20px solid red;
  border-bottom-color: #eee;
  border-top: none;
  border-left-color: transparent;
  border-right-color: transparent;
  position: relative;
  top: -12px;
  left: 174px;
}
 .inner_triangle {
  width: 0;
  height: 0;
  border: 19px solid red;
  border-bottom-color: white;
  border-top: none;
  border-left-color: transparent;
  border-right-color: transparent;
  position: relative;
  top: -31px;
  left: 175px;
  top: -31px;
  left: 175px;
}
.selectListContainer li {
  list-style-type: none;
  height: 50px;
  line-height: 50px;
  padding-left: 20px;
  background: none;
  cursor: pointer;
  color: black;
  margin-top: -38px;
}
.selectListContainer li:hover {
  background: #eee;
}
h1 {
  width: 100%;
  text-align: center;
  font-weight: bold;
  font-size: 24px;
  margin: 0;
  margin-top: 30px;
  display: inline-block;
  color: #5a5e73;
}
p {
  width: calc(100% - 200px);
  text-align: center;
  margin: auto;
  display: inline-block;
  font-size: 13px;
}
.topBar {
  background: white;
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
  background: white;
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
