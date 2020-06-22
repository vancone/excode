<template>
  <div id="app">
    <div class="top-bar">
      <h1 class="app-title">ExCRUD</h1>
    </div>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
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
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
.top-bar {
  background-color: white;
  height: 50px;
  width: 100%;
  text-align: left;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.25);
}
.app-title {
  margin: 0;
  font-size: 20px;
  line-height: 50px;
  margin-left: 20px;
}
</style>
