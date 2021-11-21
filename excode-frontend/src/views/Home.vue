<template>
  <div class="home">
    <ProjectTable/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import ProjectTable from '~/components/ProjectTable.vue'

export default defineComponent({
  name: 'Home',
  components: {
    ProjectTable
  }
})
</script>

<style scoped>
</style>
