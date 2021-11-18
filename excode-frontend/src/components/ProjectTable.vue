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
          <i class="el-icon-plus"></i>
        </el-button>
        <el-button @click="refresh" style="display: inline-block" size="mini">
          <i class="el-icon-refresh"></i>
        </el-button>
      </div>
    </div>
    <el-table :data="tableData" class="table">
      <el-table-column type="selection" width="50"> </el-table-column>
      <el-table-column label="Name" width="180">
        <template #default="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Version" width="120">
        <template #default="scope">
          <span>{{ scope.row.version }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Description" width="180">
        <template #default="scope">
          <span>{{ scope.row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Author" width="120">
        <template #default="scope">
          <span>{{ scope.row.author }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Organization" width="120">
        <template #default="scope">
          <span>{{ scope.row.organization }}</span>
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
    <ProjectDialog ref="projectDialogRef" />
  </div>
</template>

<script>
import { Delete, Edit } from '@element-plus/icons'
import ProjectDialog from './ProjectDialog.vue'
import { queryProjects, deleteProject } from '~/api/project'
import { defineComponent, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
export default defineComponent({
  name: 'ProjectTable',
  components: {
    ProjectDialog,
    Delete,
    Edit
  },
  setup () {
    const router = useRouter()
    const projectDialogRef = ref(null)
    const tableData = reactive([])
    const pagition = reactive({
      pageSize: 10,
      pageNo: 1,
      totalElements: 0
    })

    const refresh = () => {
      queryProjects({
        pageSize: pagition.pageSize,
        pageNo: pagition.pageNo
      }).then(({ data }) => {
        tableData.splice(0, tableData.length, ...data.data.content)
        pagition.totalElements = data.data.totalElements
      })
    }

    const create = () => {
      projectDialogRef.value.show(undefined, refresh)
    }

    const handleDelete = (index, row) => {
      deleteProject(row.id).then(() => {
        refresh()
      })
    }

    const handleSizeChange = (val) => {
      pagition.pageSize = val
      refresh()
    }

    const handleCurrentChange = (val) => {
      pagition.pageNo = val
      refresh()
    }

    function handleEdit (index, row) {
      router.push(`/editor/overview/${row.id}`)
    }

    return {
      tableData,
      pagition,
      projectDialogRef,
      create,
      refresh,
      handleDelete,
      handleEdit,
      handleSizeChange,
      handleCurrentChange
    }
  },
  data () {
    return {
      searchText: ''
    }
  },
  methods: {
    
  },
  mounted: function () {
    this.refresh()
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
