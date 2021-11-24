<template>
  <div class="overview-wrapper">
    <div class="metrics">
      <li>
        <h1>APIs</h1>
        <span>0</span>
      </li>
      <li>
        <h1>Data Stores</h1>
        <span>0</span>
      </li>
      <li>
        <h1>Pages</h1>
        <span>0</span>
      </li>
      <li>
        <h1>Pages</h1>
        <span>0</span>
      </li>
    </div>
    <div class="block-wrapper">
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
    </div>

    <ProjectDialog v-model:dialogVisible="projectDialogVisible" :project="project" @confirm="refresh"/>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref } from "vue";
import { queryProject } from "~/api";
import ProjectDialog from "~/components/ProjectDialog.vue";
import { IProject } from "~/api/types";
import { defaultProject } from "~/api/default-value";

export default defineComponent({
  name: "Overview",
  components: {
    ProjectDialog,
  },
  setup() {
    const projectDialogVisible = ref(false);
    const projectId = sessionStorage.getItem('projectId');
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

<style lang="scss" scoped>
.overview-wrapper {
  height: calc(100% - 100px);
  padding: 20px;
  overflow: auto;
}
.block-wrapper {
  background: white;
  padding: 20px;
  border: solid 1px #ddd;
  border-radius: 5px;
  margin: 20px 0 20px 0;
}
h1 {
  font-size: 16px;
  font-weight: 500;
}
.metrics {
  display: flex;
  justify-content: space-between;

  li {
    display: flex;
    align-items: center;
    list-style-type: none;
    background: white;
    border: solid 1px #ddd;
    border-radius: 5px;
    padding: 10px;
    width: calc(25% - 36px);
    height: 60px;

    h1 {
      display: inline;
      font-weight: bold;
      margin-right: 30px;
    }

    span {
      font-size: 36px;
      font-weight: bold;
      color: #8ba74f;
    }
  }
}
</style>
