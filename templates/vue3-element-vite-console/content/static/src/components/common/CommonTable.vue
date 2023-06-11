<template>
  <div :class="{ 'van-table-wrapper': config.showPane ?? true }">
    <div class="toolbar">
      <div class="tool-buttons">
        <el-badge v-if="config.selectMode" :value="multipleSelection.length" type="primary" class="selected-badge">
          <el-checkbox class="selected-checkbox" border label="View Selected" @change="handleCheckboxChange" />
        </el-badge>
        <el-button v-if="!config.selectMode && !config.readOnlyMode" type="primary" @click="handleCreate">{{
          config.labels?.create ??
          $t("common.table.create")
        }}</el-button>
        <el-button :disabled="multipleSelection.length == 0" v-if="!config.selectMode && !config.readOnlyMode"
          type="danger" plain @click="handleBatchDelete">{{
            config.labels?.delete ??
            $t("common.table.delete")
          }}</el-button>
        <el-button :icon="Refresh" @click="refresh" />
      </div>
      <div class="tool-buttons">
        <el-button class="button-export" v-if="config.export" @click="handleExport">
          <el-icon>
            <Download />
          </el-icon>
        </el-button>
        <el-input :placeholder="`${$t('common.table.search')}...`" v-model="searchText">
          <template #append>
            <el-button :icon="Search" @click="refresh"></el-button>
          </template>
        </el-input>
      </div>
    </div>
    <el-table :border="true" ref="tableRef" :data="tableData" v-loading="loadStatus"
      @selection-change="handleSelectionChange" class="table">
      <el-table-column type="selection" width="50" v-if="!config.readOnlyMode" />
      <el-table-column v-for="column in tableColumns" :key="column.prop" :prop="column.prop"
        :label="transformProp(column)" :width="column.width" show-overflow-tooltip>
        <template #default="scope">
          <router-link class="router-link" v-if="column.link === true"
            :to="`${config.editPageRoutePath}?id=${scope.row['id']}`">
            <span>{{
              column.transformAction == undefined
              ? scope.row[column.prop]
              : column.transformAction(scope.row[column.prop])
            }}</span>
          </router-link>
          <span v-else>{{
            column.transformAction == undefined
            ? scope.row[column.prop]
            : column.transformAction(scope.row[column.prop])
          }}</span>
          <el-tag v-if="column.type === 'tag'">{{
            scope.row[column.prop]
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Operations" width="120"
        v-if="config.operations === undefined && !(config.selectMode ?? false) && !config.readOnlyMode">
        <template #default="scope">
          <el-button link type="primary" class="operation-button" @click="handleEdit(scope.$index, scope.row)">
            Edit
          </el-button>
          <el-popconfirm title="Are you sure to delete this?" @confirm="handleDelete(scope.$index, scope.row)">
            <template #reference>
              <el-button link type="primary" class="operation-button">
                Delete
              </el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>

      <el-table-column label="Operations" width="120" v-if="config.operations !== undefined && !config.readOnlyMode">
        <template #default="scope">
          <el-button class="operation-button" link type="primary" v-for="operation in config.operations"
            :key="operation.label" @click="operation.action?.(scope.$index, scope.row) && refresh()">
            {{
              operation.label ?? operation.labelFunc?.(scope.$index, scope.row)
            }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pagination -->
    <el-pagination v-if="!viewSelected" :small="true" background @size-change="handleSizeChange"
      @current-change="handleCurrentChange" v-model:currentPage="pagination.pageNo" :page-sizes="[10, 20, 50, 100]"
      :page-size="pagination.pageSize" layout="total, sizes, prev, pager, next" :total="pagination.totalElements">
    </el-pagination>

    <!-- Edit Form Dialog -->
    <el-dialog title="Edit" v-model="dialogVisible" width="500px">
      <el-form ref="form" model="formData" label-width="auto">
        <el-form-item v-for="column in formItems" :key="column.prop" :prop="column.prop" :label="transformProp(column)">
          <el-input v-if="
            column.type === undefined ||
            column.type === 'text' ||
            column.type === 'password'
          " :type="column.type === 'password' ? 'password' : 'text'" v-model="formData[column.prop]"
            :placeholder="column.placeholder" :disabled="
              column.editable === false ||
              (column.updatable === false && dialogMode === 'update')
            " />
          <el-date-picker v-if="column.type === 'date'" type="date" v-model="formData[column.prop]"></el-date-picker>
          <el-select v-if="column.type === 'select'" v-model="formData[column.prop]">
            <el-option v-for="item in column.defaultValue" :key="item.value" :value="item.value" :label="item.label">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSave">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { Download, Refresh, Search } from '@element-plus/icons-vue';
import { computed, ComputedRef, onMounted, reactive, ref } from 'vue';
import { IAnyObject } from '~/api/types';
import { AxiosResponse } from 'axios';
import service from '~/api/request';
import { ElMessageBox, type ElTable } from 'element-plus';
import { useRouter } from 'vue-router';

export interface ITableColumn {
  prop: string;
  label?: string | ComputedRef<string>;
  type?: string; // text/password/date/select
  value?: string;
  defaultValue?: any;
  editable?: boolean;
  updatable?: boolean;
  tableColumnVisible?: boolean;
  formItemVisible?: boolean;
  placeholder?: string;
  width?: number;
  isObject?: boolean;
  link?: boolean;
  transformAction?: (name: any) => string;
}

export interface IOperationItem {
  label?: string;
  labelFunc?: (index: number, row: any) => string;
  icon?: string;
  action?: (index: number, row: any) => void;
}

export interface ILabel {
  create?: string;
  delete?: string;
}

export interface ITableConfig {
  api: string;
  createPageRoutePath?: string;
  editPageRoutePath?: string;
  creationFunc?: () => void;
  deletionFunc?: (index: number, row: IAnyObject) => void;
  beforeSubmitFunc?: (formData: any) => any;
  afterSubmitFunc?: (formData: any) => any;
  columns: Array<ITableColumn>;
  operations?: Array<IOperationItem>;
  queryParams?: IAnyObject;
  labels?: ILabel;
  selectMode?: boolean;
  readOnlyMode?: boolean;
  showPane?: boolean;
  export?: boolean;
}

function request(
  uri: string,
  method: string,
  params: IAnyObject
): Promise<AxiosResponse<IAnyObject>> {
  switch (method) {
    case 'POST':
      return service.post(uri, params);
    case 'PUT':
      return service.put(uri, params);
    case 'DELETE':
      return service.delete(uri + '/' + params);
    default:
      return service.get(uri, { params });
  }
}

const props = defineProps<{
  config: ITableConfig,
}>();

const dialogVisible = ref(false);
const router = useRouter();
const searchText = ref('');
const tableData = reactive<Array<any>>([]);
const tableDataSnapshot = reactive<Array<any>>([]);
const formData = reactive<any>({});
const tableRef = ref<InstanceType<typeof ElTable>>();
const multipleSelection = ref<IAnyObject[]>([]);
const dialogMode = ref('');
const loadStatus = ref(false);
const viewSelected = ref(false);
const pagination = reactive({
  pageSize: 10,
  pageNo: 1,
  totalElements: 0,
});

const tableColumns = computed(() => {
  return props.config.columns.filter(
    (item: ITableColumn) =>
      item.type !== 'password' && item.tableColumnVisible !== false
  );
});

const formItems = computed(() => {
  return props.config.columns.filter(
    (item: ITableColumn) => item.formItemVisible !== false
  );
});

function initFormData() {
  const columns = props.config.columns;
  for (let i in columns) {
    formData[columns[i].prop] = null;
    if (
      columns[i].tableColumnVisible == false &&
      columns[i].formItemVisible == false
    ) {
      if (columns[i].defaultValue !== undefined) {
        formData[columns[i].prop] = columns[i].defaultValue;
      }
    }
  }
}

function refresh() {
  loadStatus.value = true;
  let queryParams = { ...props.config.queryParams };
  queryParams.pageSize = pagination.pageSize;
  (queryParams.pageNo = pagination.pageNo),
    (queryParams.search = searchText.value),
    request(props.config.api, 'GET', queryParams).then(({ data }) => {
      const { list, totalCount } = data.data;
      tableData.splice(0, tableData.length, ...list);
      pagination.totalElements = totalCount;
      loadStatus.value = false;
    });
}

function handleCreate() {
  if (props.config.createPageRoutePath !== undefined) {
    router.push(props.config.createPageRoutePath);
  } else if (props.config.creationFunc !== undefined) {
    props.config.creationFunc();
  } else {
    initFormData();
    dialogMode.value = 'create';
    dialogVisible.value = true;
  }
}

function handleSave() {
  if (props.config.beforeSubmitFunc !== undefined) {
    Object.assign(formData, props.config.beforeSubmitFunc(formData));
  }
  if (dialogMode.value === 'create') {
    request(props.config.api, 'POST', formData).then(({ data }) => {
      if (data.code === 0) {
        dialogVisible.value = false;
        refresh();
        if (props.config.afterSubmitFunc !== undefined) {
          props.config.afterSubmitFunc(data.data);
        }
      }
    });
  } else {
    request(props.config.api, 'PUT', formData).then(({ data }) => {
      if (data.code === 0) {
        dialogVisible.value = false;
        refresh();
        if (props.config.afterSubmitFunc !== undefined) {
          props.config.afterSubmitFunc(data.data);
        }
      }
    });
  }
}

function handleDelete(index: number, row: IAnyObject) {
  request(props.config.api, 'DELETE', row.id).then(({ data }) => {
    if (data.code === 0) {
      dialogVisible.value = false;
      refresh();
    }
  });
}

function handleBatchDelete() {
  ElMessageBox.confirm(
    'The selected items will be deleted. Continue?',
    'Warning',
    {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  )
    .then(() => {
      for (let i in multipleSelection.value) {
        if (props.config.deletionFunc !== undefined) {
          props.config.deletionFunc(0, multipleSelection.value[i]);
        } else {
          handleDelete(0, multipleSelection.value[i]);
        }
      }
      tableRef.value!.clearSelection();
    })
    .catch(() => {
      tableRef.value!.clearSelection();
    });
}

function handleSizeChange(val: number) {
  pagination.pageSize = val;
  refresh();
}

function handleCurrentChange(val: number) {
  pagination.pageNo = val;
  refresh();
}

function handleEdit(_: number, row: any) {
  if (props.config.editPageRoutePath !== undefined) {
    router.push({
      path: props.config.editPageRoutePath,
      query: {
        id: row.id,
      },
    });
  } else {
    initFormData();
    Object.assign(formData, row);
    dialogMode.value = 'update';
    dialogVisible.value = true;
  }
}

function handleExport() {
  let exportUrl = window.location.origin + props.config.api + '/export';
  const queryParams = props.config.queryParams;
  if (queryParams !== undefined && Object.keys(queryParams).length > 0) {
    exportUrl += '?';
    Object.entries(props.config.queryParams as IAnyObject).map(item => {
      exportUrl += `${item[0]}=${item[1]}&`;
    });
  }
  window.open(exportUrl);
}

function handleSelectionChange(val: IAnyObject[]) {
  multipleSelection.value = val;
}

function transformProp(column: ITableColumn) {
  return (
    column.label ??
    column.prop.slice(0, 1).toUpperCase() + column.prop.slice(1).toLowerCase()
  );
}

function handleCheckboxChange(val: boolean) {
  if (val) {
    viewSelected.value = true;
    tableDataSnapshot.splice(0, tableDataSnapshot.length, ...tableData);
    tableData.splice(0, tableData.length, ...multipleSelection.value);
  } else {
    viewSelected.value = false;
    tableData.splice(0, tableData.length, ...tableDataSnapshot);
  }
}

onMounted(() => {
  refresh();
  initFormData();
});

defineExpose({
  multipleSelection,
  refresh,
});
</script>

<style lang="scss" scoped>
.van-table-wrapper {
  border-radius: 2px;
  background: white;
  padding: 25px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.router-link {
  text-decoration: none;
}

.router-link:hover {
  color: #b0cc66;
  text-decoration: underline;
}

.toolbar {
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tool-buttons {
  display: flex;

  .el-button {
    height: 28px;
    padding: 0 15px;
    border-radius: 2px;
    font-size: 12px;
  }

  .button-export {
    height: 30px;
    padding: 0 15px;
    margin-right: 15px;
  }

  .el-input {
    width: 250px;
    font-size: 12px;

    :deep(.el-input__inner) {
      height: 28px;
      border-radius: 2px;
    }
  }
}

.table {
  margin-bottom: 10px;
}

:deep(.table .el-table__header-wrapper th) {
  background: #e3e3e3;
  height: 28px;
  font-size: 12px;
  font-weight: 100;
  color: #555;
  text-align: left;
  border-right: solid 1px white;
}

:deep(.table .el-table__row) {
  text-align: left;
  height: 34px;
  font-size: 12px;
  font-weight: 100;
  color: #555;
}

:deep(.table .el-table__row:nth-child(even)) {
  background: #f7f7f7;
}

:deep(.table .cell) {
  line-height: 16px;
  padding-left: 10px;
}

:deep(.table .el-checkbox) {
  height: 16px;
}

:deep(.el-table td) {
  padding: 0;
}

:deep(.el-table th) {
  padding: 0;
}

:deep(.tool-buttons .el-input .el-icon) {
  color: #999;
}

:deep(.el-dialog__headerbtn .el-icon) {
  color: var(--el-color-primary);
}

.operation-button {
  font-size: 12px;
}

.operation-button:hover {
  text-decoration: underline;
}

.selected-badge {
  margin-right: 20px;
}

.selected-checkbox {
  height: 28px;
}

.operation-button {
  margin-right: 0px;
}

.operation-button:hover {
  text-decoration: underline;
}
</style>
