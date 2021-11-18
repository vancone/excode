<template>
  <Header/>
  <router-view/>
</template>

<script setup lang="ts">
import Header from './components/Header.vue'
</script>

<style>
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
</style>
