<template>
  <div class="data-store-wrapper">
    <div class="data-store-select">
      <div>
        <el-select
          v-model="currentDataStoreName"
          size="small"
          @change="handleSelect"
        >
          <el-option
            v-for="store in dataStores"
            :key="store.id"
            :label="store.name"
            :value="store.id"
          />
        </el-select>
        <el-button
          type="primary"
          size="mini"
          @click="create"
          :icon="Plus"
          style="margin-left: 20px"
        >
        </el-button>
        <el-button
          type="danger"
          size="mini"
          style="margin-left: 10px"
          :icon="Delete"
          @click="remove"
        >
        </el-button>
        <el-button
          size="mini"
          style="margin-left: 10px"
          :icon="Refresh"
          @click="refresh"
        >
        </el-button>
      </div>
    </div>
    <div class="block-wrapper">
      <el-descriptions class="margin-top" title="Properties" :column="2" size="small" border>
        <template #extra>
          <el-button type="primary" size="mini" @click="edit">Edit</el-button>
        </template>
        <el-descriptions-item>
          <template #label> Name </template>
          {{ dataStore.name }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label> Description </template>
          {{ dataStore.description }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label> Type </template>
          {{ dataStore.type }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label> Carrier </template>
          {{ dataStore.carrier }}
        </el-descriptions-item>
        
        <el-descriptions-item>
          <template #label> Creator </template>
          -
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label> Created Time </template>
          {{ dataStore.createdTime }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label> Last Updator </template>
          -
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label> Last Updated Time </template>
          {{ dataStore.updatedTime }}
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <div class="block-wrapper">
      <div class="table-top-bar">
        <h1>Nodes</h1>
        <div>
          <el-button type="primary" size="mini" @click="showCreateNodeDialog"
            >Add Node</el-button
          >
          <el-button type="primary" size="mini" @click="generateSql"
            >Generate SQL</el-button
          >
        </div>
      </div>

      <el-table :data="dataStore.nodes" border size="small">
        <el-table-column label="Name" width="200">
          <template #default="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Type" width="180">
          <template #default="scope">
            <span>{{ scope.row.type }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Length" width="180">
          <template #default="scope">
            <span>{{ scope.row.length }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Commnet">
          <template #default="scope">
            <span>{{ scope.row.comment }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Operations">
          <template #default="scope">
            <el-button
              type="text"
              size="mini"
              @click="showUpdateNodeDialog(scope.$index, scope.row)"
            >
              <el-icon size="14" color="#b0cc66" title="Edit">
                <edit />
              </el-icon>
            </el-button>
            <el-popconfirm
              title="Are you sure to delete this?"
              @confirm="handleDelete(scope.$index, scope.row)"
            >
              <template #reference>
                <el-button type="text" size="mini">
                  <el-icon size="14" color="#b0cc66" title="Delete">
                    <delete />
                  </el-icon>
                </el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <DataStoreDialog
      v-model:dialogVisible="createDataStoreDialogVisible"
      @confirm="refresh"
    />
    <DataStoreDialog
      v-model:dialogVisible="updateDataStoreDialogVisible"
      :dataStore="dataStore"
      @confirm="refresh"
    />
    <DataStoreNodeDialog
      v-model:dialogVisible="createDataStoreNodeDialogVisible"
      @confirm="createNode"
    />
    <DataStoreNodeDialog
      v-model:dialogVisible="updateDataStoreNodeDialogVisible"
      :dataStoreNode="dataStoreNode"
      @confirm="updateNode"
    />

    <el-dialog
      v-model="generateSqlDialogVisible"
      title="Generated SQL"
      width="400px"
    >
      <pre>{{ generatedSql }}</pre>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" size="mini" @click="generateSqlDialogVisible = false">OK</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { ElDialog, ElMessage, ElMessageBox } from "element-plus";
import { Delete, Edit, Plus, Refresh } from "@element-plus/icons";
import { defineComponent, onMounted, reactive, ref } from "vue";
import { useRoute } from "vue-router";
import { defaultDataStore, defaultDataStoreNode } from "~/api/default-value";
import {
  deleteDataStore,
  queryDataStores,
  updateDataStore,
  generateDataStoreSql,
} from "~/api/index";
import { IDataStore, IDataStoreNode } from "~/api/types";
import DataStoreDialog from "./DataStoreDialog.vue";
import DataStoreNodeDialog from "./DataStoreNodeDialog.vue";

export default defineComponent({
  name: "DataStore",
  components: {
    DataStoreDialog,
    DataStoreNodeDialog,
    Delete,
    Edit,
    ElDialog,
    Plus,
    Refresh,
  },
  setup() {
    const dataStores = reactive<Array<IDataStore>>([]);
    const currentDataStoreName = ref("");
    const dataStore = reactive<IDataStore>({ ...defaultDataStore });
    const dataStoreNode = reactive<IDataStoreNode>({ ...defaultDataStoreNode });
    const createDataStoreDialogVisible = ref(false);
    const updateDataStoreDialogVisible = ref(false);
    const createDataStoreNodeDialogVisible = ref(false);
    const updateDataStoreNodeDialogVisible = ref(false);
    const generateSqlDialogVisible = ref(false);
    const updatedDataStoreIndex = ref(0);
    const generatedSql = ref("");

    function handleSelect(val: string) {
      Object.assign(
        dataStore,
        dataStores.find((item) => {
          return item.id === val;
        })
      );
      currentDataStoreName.value = dataStore.name;
    }

    function create() {
      createDataStoreDialogVisible.value = true;
    }

    function edit() {
      updateDataStoreDialogVisible.value = true;
    }

    function remove() {
      ElMessageBox.confirm(
        `DataStore \"${currentDataStoreName.value}\" will be permanently deleted. Continue?`,
        "Warning",
        {
          confirmButtonText: "Confirm",
          cancelButtonText: "Cancel",
          type: "warning",
        }
      ).then(() => {
        deleteDataStore(dataStore.id).then(() => {
          refresh();
        });
      });
    }

    function save() {
      updateDataStore(dataStore).then(refresh);
    }

    function showCreateNodeDialog() {
      createDataStoreNodeDialogVisible.value = true;
    }

    function showUpdateNodeDialog(index: number, row: IDataStoreNode) {
      Object.assign(dataStoreNode, row);
      updatedDataStoreIndex.value = index;
      updateDataStoreNodeDialogVisible.value = true;
    }

    function createNode(node: IDataStoreNode) {
      dataStore.nodes.push(node);
      save();
    }

    function updateNode(node: IDataStoreNode) {
      Object.assign(dataStore.nodes[updatedDataStoreIndex.value], node);
      save();
    }

    function refresh() {
      queryDataStores({
        projectId: sessionStorage.getItem('projectId'),
      }).then(({ data }) => {
        dataStores.splice(0, dataStores.length, ...data.data);
        Object.assign(dataStore, dataStores[0]);
        currentDataStoreName.value = dataStore.name;
      });
    }

    function handleDelete(index: number, row: IDataStoreNode) {
      dataStore.nodes.splice(index, 1);
      save();
    }

    function generateSql() {
      generateDataStoreSql(dataStore.id).then(({ data }) => {
        generatedSql.value = data.data;
        generateSqlDialogVisible.value = true;
      });
    }

    onMounted(refresh);

    return {
      createDataStoreDialogVisible,
      currentDataStoreName,
      updateDataStoreDialogVisible,
      createDataStoreNodeDialogVisible,
      updateDataStoreNodeDialogVisible,
      dataStores,
      dataStore,
      dataStoreNode,
      Delete,
      generatedSql,
      generateSqlDialogVisible,
      Plus,
      Refresh,
      create,
      createNode,
      remove,
      edit,
      generateSql,
      handleDelete,
      handleSelect,
      refresh,
      showCreateNodeDialog,
      showUpdateNodeDialog,
      updateNode,
    };
  },
});
</script>

<style lang="scss" scoped>
.data-store-wrapper {
  padding: 0 20px 0 20px;
}
.data-store-select {
  margin-top: 20px;
  margin-bottom: 20px;
}
.block-wrapper {
  background: white;
  padding: 20px;
  border: solid 1px #ddd;
  border-radius: 5px;
  margin: 20px 0 20px 0;
}
:deep(.el-table__row td) {
  border-right: solid 1px #ebeef5;
  border-bottom: solid 1px #ebeef5;
  height: 40px;
}
.table-top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 16px;

  h1 {
    font-size: 16px;
    font-weight: bold;
  }
}
:deep(.el-dialog__body) {
  padding-top: 0;
}
</style>
