<template>
  <Header/>
  <router-view/>
</template>

<script>
import Header from '@/components/Header.vue'

export default {
  name: 'Home',
  components: {
    Header
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  height: 100%;
  background: transparent;
}

/* #nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
} */
.header {
  background: black;
  color: white;
  height: 54px;
  text-align: left;
  padding-left: 10px;
}
h1 {
  margin-top: 0;
  margin-left: 10px;
  font-size: 22px;
  line-height: 55px;
  font-weight: 100;
  cursor: default;
  display: inline-block;
}
.logo {
  height: 35px;
  line-height: 55px;
  vertical-align: top;
  margin-top: 9px;
  display: inline-block;
}
</style>
