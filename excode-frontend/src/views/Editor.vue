<template>
  <div class="editor">
    <ToolBar />
    <main>
      <EditorMenu />
      <router-view class="container"></router-view>
    </main>

    <ExportDialog />
  </div>
</template>

<script lang="ts">
import ToolBar from '~/components/editor/ToolBar.vue'
import DataTablePanel from '~/components/editor/DataTablePanel.vue'
import EditorMenu from '~/components/editor/EditorMenu.vue'
import { defineComponent, onBeforeUnmount, onMounted } from 'vue'
import { queryProject } from '~/api'
import { loadProject, setProject } from '~/api/store'
export default defineComponent({
  name: 'Editor',
  components: {
    ToolBar,
    EditorMenu,
    DataTablePanel
  },
  setup() {
    onMounted(() => {
      loadProject()
    })
    onBeforeUnmount(() => {
        alert(1024)
    })
  }
})
</script>

<style scoped>
.editor {
  height: calc(100% - 50px);
  width: 100%;
  background: #f5f5f5;
}
main {
  display: flex;
  height: calc(100% - 35px);
}
.container {
  width: calc(100% - 240px);
  height: calc(100%);
  overflow-y: auto;
}
</style>
