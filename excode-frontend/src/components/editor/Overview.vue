<template>
  <div class="data-access-panel">
    <div class="property-block">
      <div class="header">
        <h1>General</h1>
        <el-button type="primary" size="mini" @click="updateInfo">Edit</el-button>
      </div>
      <el-table
        :data="projectInfo"
        :show-header="false"
        border
        style="width: 100%"
      >
        <el-table-column prop="key" width="180" style="background: #f5f7fa" />
        <el-table-column prop="value" />
      </el-table>
    </div>

    <ProjectDialog ref="projectDialogRef"/>
  </div>
</template>

<script>
import { defineComponent, onMounted, reactive, ref } from 'vue'
import { queryProject } from '@/api/project'
import ProjectDialog from '@/components/ProjectDialog'
import { getUrlParam } from '@/util/utils'
export default defineComponent({
  name: 'Overview',
  components: {
    ProjectDialog
  },
  setup () {
    const projectDialogRef = ref(null)

    const projectInfo = reactive([
      { key: 'Name', value: '' },
      { key: 'Version', value: '' },
      { key: 'Description', value: '' },
      { key: 'Author', value: '' },
      { key: 'Organization', value: '' }
    ])

    const refresh = () => {
      const projectId = getUrlParam('id')
      if (projectId !== null && projectId !== '') {
        queryProject(projectId).then(({ data }) => {
          const content = [
            { key: 'Name', value: data.data.name },
            { key: 'Version', value: data.data.version },
            { key: 'Description', value: data.data.description },
            { key: 'Author', value: data.data.author },
            { key: 'Organization', value: data.data.organization }
          ]
          projectInfo.splice(0, projectInfo.length, ...content)
        })
      }
    }

    const updateInfo = () => {
      console.log(projectDialogRef.value)
      projectDialogRef.value.show(getUrlParam('id'), refresh)
    }

    onMounted(() => {
      console.log(projectDialogRef)
      refresh()
    })

    return {
      projectInfo,
      updateInfo,
      projectDialogRef
    }
  }
})
</script>

<style scoped>
.data-access-panel {
  height: calc(100% - 20px);
  overflow: auto;
}
.header {
  display: flex;
  justify-content: space-between;
  padding: 10px 0 10px 0;
  height: 30px;
}
:deep(.el-tabs__nav-scroll) {
  background: #f9f9fa;
  height: 30px;
  border-bottom: solid 1px #ddd;
}
:deep(.el-tabs--card > .el-tabs__header .el-tabs__item) {
  height: 30px;
  line-height: 30px;
}
:deep(.el-tabs--card > .el-tabs__header .el-tabs__item) {
  border-left: none;
  border-right: solid 1px #ddd;
}
:deep(.el-tabs__item.is-active) {
  border-right: solid 1px #ddd;
  background-color: white;
}
:deep(.el-tabs__header) {
  margin: 0;
}
:deep(.el-tabs--card > .el-tabs__header .el-tabs__nav) {
  border: none;
}
:deep(.el-table td) {
  padding: 5px;
}
.tabs {
  height: inherit;
  overflow: hidden;
  padding: 0;
  margin: 0;
}
h1 {
  font-size: 16px;
  font-weight: 500;
}
.property-block {
  margin-bottom: 25px;
  margin-left: 20px;
  margin-right: 20px;
}
</style>
