<template>
  <div class="home">
    <ProjectList/>
  </div>
</template>

<script>
// @ is an alias to /src
import ProjectList from '@/components/ProjectList.vue'

export default {
  name: 'Home',
  components: {
    ProjectList
  }
}
</script>

<style scoped>
</style>
