<template>
  <div class="application-table">
    <h1 class="title">Table</h1>
    <CommonTable :config="tableConfig" />
  </div>

</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { IApplication } from "~/api/types";
import CommonTable, { ITableConfig } from "~/components/common/CommonTable.vue";

const updateDialogVisible = ref(false);
const activeIndex = ref(0);

const tableConfig = reactive<ITableConfig>({
  api: '/api/passport/admin/v1/user-group',
  createPageRoutePath: undefined,
  editPageRoutePath: '/user-group/detail',
  columns: [
    { prop: 'name', link: true },
    { prop: 'description' },
    { prop: 'createdTime', label: 'Created Time', formItemVisible: false },
  ]
});

function handleEdit(index: number, row: IApplication) {
  updateDialogVisible.value = true;
  activeIndex.value = index
}
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
