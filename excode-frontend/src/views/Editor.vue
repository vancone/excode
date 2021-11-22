<template>
  <div class="editor">
    <ToolBar />
    <main>
      <EditorMenu />
      <router-view class="container"></router-view>
      <div class="data-table-panel">
        <data-table-panel />
      </div>
    </main>

    <ExportDialog />
  </div>
</template>

<script lang="ts">
import ToolBar from '~/components/editor/ToolBar.vue'
import DataTablePanel from '~/components/editor/DataTablePanel.vue'
import EditorMenu from '~/components/editor/EditorMenu.vue'
import { defineComponent } from 'vue'
export default defineComponent({
  name: 'Editor',
  components: {
    ToolBar,
    EditorMenu,
    DataTablePanel
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
  width: calc(100% - 450px);
  height: calc(100%);
  overflow-y: auto;
}
.data-table-panel {
  background: #fff;
  height: 100%;
  width: 250px;
  text-align: left;
  border-left: solid 1px #ddd;
}
</style>
