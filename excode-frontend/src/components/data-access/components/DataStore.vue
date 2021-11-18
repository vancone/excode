<template>
  <div class="solution-select">
    <div>
      <span style="margin: 0 20px 0 20px">Data Store</span>
      <el-select v-model="targetDataStore" size="small">
        <el-option
          v-for="store in dataStores"
          :key="store.id"
          :label="store.name"
          :value="store.id"
        />
      </el-select>
      <el-button type="primary" size="mini" style="margin-left:20px;">Create</el-button>
      <el-button type="danger" size="mini" style="margin-left:10px;">Delete</el-button>
    </div>
  </div>
  <div class="property-block">
    <h1>Properties</h1>
    <el-table
      :data="pomTableData"
      :show-header="false"
      border
      style="width: 100%"
    >
      <el-table-column prop="key" width="180" style="background: #f5f7fa" />
      <el-table-column prop="value" />
    </el-table>
  </div>

  <div class="property-block">
    <h1>Entity</h1>
    <el-table
      :data="middlewareTableData"
      :show-header="false"
      border
      style="width: 100%"
    >
      <el-table-column prop="key" width="180" />
      <el-table-column prop="value" />
    </el-table>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { queryDataStore } from '~/api/data-source.js';

export default defineComponent({
  name: "DataStore",
  setup() {
    const dataStores = ref([]);
    const targetDataStore = ref('');

    onMounted(() => {
      queryDataStore({
        projectId: useRoute().params.projectId,
      }).then(({ data }) => {
        dataStores.value = data.data;
      })
    })

    return {
      dataStores,
      targetDataStore,
    };
  },
});
</script>

<style scoped>
h1 {
  font-size: 16px;
  font-weight: 500;
}
.property-block {
  margin-bottom: 25px;
  margin-left: 20px;
  margin-right: 20px;
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
