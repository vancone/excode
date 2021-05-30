<template>
  <div class="header">
    <h1>ExCRUD</h1>
  </div>
  <router-view/>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
.header {
  background: black;
  color: white;
  height: 54px;
  text-align: left;
  padding-left: 20px;
}

h1 {
  margin-top: 0;
  font-size: 22px;
  line-height: 54px;
  font-weight: 100;
  cursor: default;
}
</style>
