<template>
  <div id="app">
    <Panel :project="project"/>
    <TopBar :title="title"/>
    <router-view :project="project" v-on:submitProject="updateProject" id="routerViewContainer"/>
  </div>
</template>

<script>
import Panel from "@/components/Panel"
import TopBar from "@/components/TopBar"
export default {
  name: 'App',
  components: {Panel, TopBar},
  data() {
    return {
      title: this.$route.name,
      project: null
    }
  },
  methods: {
    updateProject(project) {
      this.project = project

    }
  },
  watch: {
    '$route' (to, from) {
      this.title = to.name
    },
    project (val, oldVal) {
      alert(JSON.stringify(val))
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background: #edf1f2;
  height: 100%;
  width: 100%;
  overflow: hidden;
}
#routerViewContainer {
  position: fixed;
  left: 200px;
  top: 50px;
}
</style>
