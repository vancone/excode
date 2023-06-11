<template>
  <div class="application-table">
    <h1 class="title">${ModelName}</h1>
    <CommonTable :config="tableConfig" />
  </div>

</template>

<script setup lang="ts">
import { reactive } from "vue";
import CommonTable, { ITableConfig } from "~/components/common/CommonTable.vue";

const tableConfig = reactive<ITableConfig>({
  api: '${template.properties.apiPath}/${modelName}',
  columns: [
${columns}
    { prop: 'createdTime', label: 'Created Time', formItemVisible: false },
    { prop: 'updatedTime', label: 'Updated Time', formItemVisible: false },
  ]
});
</script>

<style lang="scss" scoped>
.application-table {
  // background-color: white;
  // border: solid 1px #eee;
  // border-radius: 2px;
  box-sizing: border-box;
}

.title {
  font-weight: 400;
  font-size: 18px;
  margin-top: 0;
}

.searchbox {
  height: 20px;
  width: 240px;
  margin-right: 10px;
}

.toolbar {
  height: 50px;
  margin-left: 10px;
  margin-right: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tool-buttons {
  display: flex;

  button {
    width: 40px;
    padding: 0;
    border-radius: 2px;
  }
}
</style>
