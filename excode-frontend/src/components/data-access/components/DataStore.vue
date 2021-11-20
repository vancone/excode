<template>
  <div class="data-store-wrapper">
    <div class="data-store-select">
      <div>
        <h1 style="margin-right: 20px">Data Store</h1>
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
          style="margin-left: 20px"
          >Create</el-button
        >
        <el-button
          type="danger"
          size="mini"
          style="margin-left: 10px"
          @click="remove"
          >Delete</el-button
        >
      </div>
    </div>
    <el-descriptions class="margin-top" title="Properties" :column="4" border>
      <template #extra>
        <el-button type="primary" size="mini" @click="edit">Edit</el-button>
      </template>
      <el-descriptions-item span="2">
        <template #label> Name </template>
        {{ dataStore.name }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label> Type </template>
        {{ dataStore.type }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label> Carrier </template>
        {{ dataStore.carrier }}
      </el-descriptions-item>
      <el-descriptions-item span="4">
        <template #label> Description </template>
        {{ dataStore.description }}
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

    <div class="property-block">
      <h1>Entity</h1>
      <el-table :data="dataStore.nodes" border style="width: 100%">
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
              @click="handleEdit(scope.$index, scope.row)"
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

    <DataStoreDialog ref="dataStoreDialogRef" />
  </div>
</template>

<script lang="ts">
import { ElDialog, ElMessageBox } from "element-plus";
import { Delete, Edit } from '@element-plus/icons'
import { defineComponent, onMounted, reactive, ref } from "vue";
import { useRoute } from "vue-router";
import { defaultDataStore } from "~/api/default-value";
import { deleteDataStore, queryDataStores } from "~/api/index";
import { IDataStore } from "~/api/types";
import DataStoreDialog from "./DataStoreDialog.vue";

export default defineComponent({
  name: "DataStore",
  components: {
    DataStoreDialog,
    Delete,
    Edit
  },
  setup() {
    const route = useRoute();
    const dataStores = reactive<Array<IDataStore>>([]);
    const currentDataStoreName = ref("");
    const dataStore = reactive<IDataStore>({ ...defaultDataStore });
    const dataStoreDialogRef = ref<InstanceType<typeof ElDialog> | null>(null);

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
      dataStoreDialogRef.value.show(null, refresh);
    }

    function edit() {
      dataStoreDialogRef.value.show(dataStore.id, refresh);
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

    function refresh() {
      queryDataStores({
        projectId: route.params.projectId,
      }).then(({ data }) => {
        dataStores.splice(0, dataStores.length, ...data.data);
        Object.assign(dataStore, dataStores[0]);
        currentDataStoreName.value = dataStore.name;
      });
    }

    onMounted(refresh);

    return {
      dataStoreDialogRef,
      dataStores,
      dataStore,
      currentDataStoreName,
      create,
      remove,
      edit,
      handleSelect,
    };
  },
});
</script>

<style scoped>
.data-store-wrapper {
  padding: 0 20px 0 20px;
}
.data-store-select {
  margin-bottom: 20px;
}
h1 {
  font-size: 16px;
  font-weight: bold;
  margin-top: 25px;
  margin-bottom: 25px;
}
.property-block {
  margin-bottom: 25px;
}
:deep(.el-table__row td) {
  border-right: solid 1px #ebeef5;
  border-bottom: solid 1px #ebeef5;
  height: 40px;
}
.solution-select {
  margin-top: 20px;
}
</style>
