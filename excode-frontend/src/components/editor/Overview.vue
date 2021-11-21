<template>
  <div class="overview-wrapper">
    <el-descriptions
      class="margin-top"
      title="General"
      :column="2"
      size="small"
      border
    >
      <template #extra>
        <el-button type="primary" size="mini" @click="edit"
          >Edit</el-button
        >
      </template>
      <el-descriptions-item>
        <template #label> Name </template>
        {{ project.name }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label> Version </template>
        {{ project.version }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label> Author </template>
        {{ project.author }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label> Orginization </template>
        {{ project.organization }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label> Description </template>
        {{ project.description }}
      </el-descriptions-item>
    </el-descriptions>

    <ProjectDialog v-model:dialogVisible="projectDialogVisible" :project="project" @confirm="refresh"/>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref } from "vue";
import { queryProject } from "~/api";
import ProjectDialog from "~/components/ProjectDialog.vue";
import { useRoute } from "vue-router";
import { IProject } from "~/api/types";
import { defaultProject } from "~/api/default-value";

export default defineComponent({
  name: "Overview",
  components: {
    ProjectDialog,
  },
  setup() {
    const projectDialogVisible = ref(false);
    const projectId = useRoute().params.projectId as string;
    const project = reactive<IProject>({...defaultProject});

    const refresh = () => {
      if (projectId !== null && projectId !== "") {
        queryProject(projectId).then(({ data }) => {
          Object.assign(project, data.data)
        });
      }
    };

    const edit = () => {
      projectDialogVisible.value = true;
    };

    onMounted(() => {
      refresh();
    });

    return {
      project,
      projectDialogVisible,
      edit,
      refresh
    };
  },
});
</script>

<style scoped>
.overview-wrapper {
  height: calc(100% - 60px);
  padding: 20px;
  overflow: auto;
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
h1 {
  font-size: 16px;
  font-weight: 500;
}
</style>
