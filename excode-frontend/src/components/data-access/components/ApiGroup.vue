<template>
  <div class="data-store-wrapper">
    <div class="data-store-select" style="display: none">
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

    <div class="toolbar-wrapper">
      <el-button type="primary" size="mini" @click="create" :icon="Download">
        Postman Collection
      </el-button>
    </div>

    <el-collapse class="collapse" v-model="activeNames" @change="handleChange">
      <el-collapse-item
        v-for="(item, index) in dataStores"
        :key="index"
        :title="item.name"
        :name="index"
      >
        <template #title>
          <h1>{{ item.name }}</h1>
          <el-icon style="color: #666; font-size: 16px"
            ><question-filled
          /></el-icon>
        </template>
        <el-table :data="apiList" border size="small">
          <el-table-column label="Status" width="80">
            <template #default="scope">
              <el-switch v-model="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column label="Method" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.tagType" size="mini">{{
                scope.row.method
              }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="Name" width="150">
            <template #default="scope">
              <span>{{ scope.row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="Path" width="150">
            <template #default="scope">
              <span>{{ scope.row.path }}</span>
            </template>
          </el-table-column>
          <el-table-column label="Params" width="150">
            <template #default="scope">
              <span>{{ scope.row.params }}</span>
            </template>
          </el-table-column>
          <el-table-column label="SQL" width="300">
            <template #default="scope">
              <span>{{ scope.row.sql }}</span>
            </template>
          </el-table-column>
          <el-table-column label="Description">
            <template #default="scope">
              <span>{{ scope.row.description }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-collapse-item>
    </el-collapse>

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
          <el-button
            type="primary"
            size="mini"
            @click="generateSqlDialogVisible = false"
            >OK</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { ElDialog, ElMessage, ElMessageBox } from "element-plus";
import {
  Delete,
  Download,
  Edit,
  Plus,
  QuestionFilled,
  Refresh,
} from "@element-plus/icons";
import { defineComponent, onMounted, reactive, ref } from "vue";
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
    Download,
    Edit,
    ElDialog,
    Plus,
    QuestionFilled,
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
    const apiList = [
      {
        status: true,
        method: "GET",
        tagType: "info",
        name: "Query",
        path: "/api/employee/{id}",
        params: "id",
        sql: "SELECT * FROM employee WHERE id = ${id}",
      },
      {
        status: true,
        method: "GET",
        tagType: "info",
        name: "Query Page",
        path: "/api/employee",
        params: "pageSize, pageNo",
        sql: "SELECT * FROM employee LIMIT ${pageSize} OFFSET (${pageNo} - 1) * ${pageSize}",
      },
      {
        status: true,
        method: "POST",
        tagType: "success",
        name: "Create",
        path: "/api/employee",
        sql: "INSERT INTO employee VALUES(${body})",
      },
      {
        status: true,
        method: "PUT",
        tagType: "warning",
        name: "Update",
        path: "/api/employee",
        sql: "UPDATE employee SET name=${body.name} WHERE id = ${id}",
      },
      {
        status: false,
        method: "DELETE",
        tagType: "danger",
        name: "Delete",
        path: "/api/employee/{id}",
        params: "id",
        sql: "DELETE FROM employee WHERE id = ${id}",
      },
    ];

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
        projectId: sessionStorage.getItem("projectId"),
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
      apiList,
      createDataStoreDialogVisible,
      currentDataStoreName,
      updateDataStoreDialogVisible,
      createDataStoreNodeDialogVisible,
      updateDataStoreNodeDialogVisible,
      dataStores,
      dataStore,
      dataStoreNode,
      Delete,
      Download,
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
  padding: 20px 20px 0 20px;
  height: calc(100% - 20px);
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
:deep(.el-collapse-item__header) {
  padding-left: 15px;
  padding-right: 10px;
}
:deep(.el-collapse-item__content) {
  padding: 15px;
}
.collapse {
  h1 {
    font-size: 18px;
    margin-right: 10px;
  }
}

.toolbar-wrapper {
  margin-bottom: 10px;
}
</style>
