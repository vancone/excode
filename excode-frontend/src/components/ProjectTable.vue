<template>
  <div class="project-table">
    <div class="toolbar">
      <h1 class="title">Projects</h1>
      <div class="tool-buttons">
        <el-input
          placeholder="Search..."
          v-model="searchText"
          size="mini"
          class="searchbox"
        >
          <template #suffix>
            <i class="el-icon-search el-input__icon" @click="refresh"></i>
          </template>
        </el-input>
        <el-button
          type="primary"
          style="display: inline-block"
          @click="create"
          size="mini"
        >
          <el-icon><plus /></el-icon>
        </el-button>
        <el-button @click="refresh" style="display: inline-block" size="mini">
          <el-icon><refresh /></el-icon>
        </el-button>
      </div>
    </div>
    <el-table :data="tableData" class="table">
      <el-table-column type="selection" width="50"> </el-table-column>
      <el-table-column label="Name" width="220">
        <template #default="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Version" width="120">
        <template #default="scope">
          <span>{{ scope.row.version }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Author" width="150">
        <template #default="scope">
          <span>{{ scope.row.author }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Organization" width="150">
        <template #default="scope">
          <span>{{ scope.row.organization }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Description" width="300">
        <template #default="scope">
          <span>{{ scope.row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Last Updated" width="180">
        <template #default="scope">
          <span>{{ scope.row.updatedTime }}</span>
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

    <!-- Pagition -->
    <el-pagination
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      v-model:currentPage="pagition.pageNo"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pagition.pageSize"
      layout="total, sizes, prev, pager, next"
      :total="pagition.totalElements"
    >
    </el-pagination>

    <!-- Create Project Dialog -->
    <ProjectDialog v-model:dialogVisible="projectDialogVisible" @confirm="refresh"/>
  </div>
</template>

<script lang="ts">
import { Delete, Edit, Plus, Refresh } from '@element-plus/icons'
import ProjectDialog from './ProjectDialog.vue'
import { queryProjects, deleteProject } from '~/api'
import { defineComponent, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { IAnyObject, IProject } from '~/api/types'
export default defineComponent({
  name: 'ProjectTable',
  components: {
    ProjectDialog,
    Delete,
    Edit,
    Plus,
    Refresh
  },
  setup () {
    const router = useRouter();
    const projectDialogVisible = ref(false);
    const searchText = ref('');
    const tableData = reactive<Array<IProject>>([]);
    const pagition = reactive({
      pageSize: 10,
      pageNo: 1,
      totalElements: 0
    });

    function refresh() {
      queryProjects({
        pageSize: pagition.pageSize,
        pageNo: pagition.pageNo
      }).then(({ data }) => {
        const { list, totalCount} = data.data;
        tableData.splice(0, tableData.length, ...list);
        pagition.totalElements = totalCount;
      })
    }

    function create() {
      projectDialogVisible.value = true
    }

    function handleDelete(index: number, row: IAnyObject) {
      deleteProject(row.id).then(() => {
        refresh();
      });
    }

    function handleSizeChange(val: number) {
      pagition.pageSize = val;
      refresh();
    }

    function handleCurrentChange(val: number) {
      pagition.pageNo = val;
      refresh();
    }

    function handleEdit (index: number, row: IProject) {
      router.push(`/editor/overview/${row.id}`);
    }

    onMounted(refresh);

    return {
      tableData,
      pagition,
      projectDialogVisible,
      searchText,
      create,
      refresh,
      handleDelete,
      handleEdit,
      handleSizeChange,
      handleCurrentChange
    }
  }
})
</script>

<style scoped>
.project-table {
  background-color: white;
  border: solid 1px #eee;
  border-radius: 2px;
  box-sizing: border-box;
  margin: 10px;
  padding: 10px;
}
.title {
  font-weight: 400;
  font-size: 18px;
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
.table {
  width: calc(100% - 10px);
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
</style>
