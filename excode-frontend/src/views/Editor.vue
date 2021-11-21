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

    <!-- Create data table dialog -->
    <el-dialog
      title="Data Table"
      v-model="dataTableDialogVisible"
      width="50%"
      :before-close="handleClose"
    >
      <el-form
        ref="form"
        :model="dataObject"
        label-width="140px"
        label-position="left"
      >
        <el-form-item label="Table Name">
          <el-input
            v-model="dataObject.value"
            maxlength="20"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item label="Data Source Type">
          <el-select
            v-model="dataObject.type"
            @change="toggleDataSourceType"
            style="width: 100%"
          >
            <el-option label="Elasticsearch" value="ELASTICSEARCH"></el-option>
            <el-option label="MariaDB / MySQL" value="MYSQL"></el-option>
            <el-option label="MongoDB" value="MONGODB"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Data Source">
          <el-select v-model="dataObject.name" style="width: 100%">
            <el-option
              v-for="item in dataSourceOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dataTableDialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="save">Save</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- Data source dialog -->
    <el-dialog
      title="Data Source"
      v-model="dataSourceDialogVisible"
      width="70%"
      :before-close="handleClose"
    >
      <data-source-panel />
    </el-dialog>

    <!-- Data table key dialog -->
    <el-dialog
      title="Data Table Key"
      v-model="dataTableKeyDialogVisible"
      width="40%"
      :before-close="handleClose"
    >
      <el-form
        ref="dataTableKey"
        :model="dataObject"
        label-width="80px"
        style="text-align: left"
      >
        <el-form-item label="Value">
          <el-input v-model="dataTableKey.value"></el-input>
        </el-form-item>
        <el-form-item label="Type">
          <el-select
            v-model="dataTableKey.type"
            placeholder=""
            @change="toggleDataSourceType"
          >
            <el-option label="Elasticsearch" value="ELASTICSEARCH"></el-option>
            <el-option label="MariaDB / MySQL" value="MYSQL"></el-option>
            <el-option label="MongoDB" value="MONGODB"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Source" v-if="dataTableKey.root">
          <span v-text="dataTableKey.source"></span>
          <el-button
            type="primary"
            style="float: right"
            size="small"
            @click="openSelectDataSourceDialog"
            >Select</el-button
          >
        </el-form-item>
      </el-form>
      <el-dialog
        title="Select..."
        v-model="dataTableKeySelectDataSourceDialogVisible"
      >
        <data-source-panel />
        <template #footer>
          <span class="dialog-footer">
            <el-button
              @click="dataTableKeySelectDataSourceDialogVisible = false"
              >Cancel</el-button
            >
            <el-button type="primary" @click="saveDataTableKey">OK</el-button>
          </span>
        </template>
      </el-dialog>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dataTableKeyDialogVisible = false"
            >Cancel</el-button
          >
          <el-button type="primary" @click="saveDataTableKey">Save</el-button>
        </span>
      </template>
    </el-dialog>
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
