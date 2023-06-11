<template>
  <Header v-if="route.query.embedded !== 'true'" />
  <div class="container">
    <Menu title="${project.Name}" v-if="route.query.embedded !== 'true'" />
    <main class="wrapper"><router-view/></main>
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router';
import Header from './components/common/Header.vue';
import Menu from './components/common/SideMenu.vue';

const route = useRoute();
</script>

<style>
html, body {
  height: 100%;
  width: 100%;
  min-width: 768px;
  margin: 0;
  background: #f0f2f5;
  font-family: Arial, Helvetica, sans-serif;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  height: 100%;
  background: transparent;
}

li.el-select-dropdown__item span {
  font-family: Arial, Helvetica, sans-serif;
}

.container {
  display: flex;
  height: calc(100% - 50px);
  widows: 100%;
}

.wrapper {
  width: calc(100% - 250px);
  padding: 25px;
}
</style>
