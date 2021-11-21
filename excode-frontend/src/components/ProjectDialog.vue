<template>
  <el-dialog
    :title="dialogTitle"
    :model-value="dialogVisible"
    class="dialog"
    :before-close="handleClose"
  >
    <el-form ref="formRef" :model="project" label-width="auto">
      <el-form-item label="Name">
        <el-input v-model="project.name" size="small"></el-input>
      </el-form-item>
      <el-form-item label="Version">
        <el-input v-model="project.version" size="small"></el-input>
      </el-form-item>
      <el-form-item label="Author">
        <el-input v-model="project.author" size="small"></el-input>
      </el-form-item>
      <el-form-item label="Organization">
        <el-input v-model="project.organization" size="small"></el-input>
      </el-form-item>
      <el-form-item label="Description">
        <el-input v-model="project.description" size="small"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button size="small" @click="$emit('update:dialogVisible', $event.target.value)">Cancel</el-button>
      <el-button type="primary" size="small" @click="save">Save</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from 'vue'
import { ElMessageBox } from 'element-plus'
import { createProject } from '~/api'
import { IProject } from '~/api/types'
import { defaultProject } from '~/api/default-value'

export default defineComponent({
  name: 'ProjectDialog',
  props: {
    dialogVisible: Boolean,
    project: Object
  },
  setup (props, { emit }) {
    const project = reactive<IProject>({...defaultProject});
    const dialogTitle = ref('New Project');

    const save = () => {
      const project = props.project as IProject;
      createProject(project).then(({ data }) => {
        if (data.code === 0) {
          Object.assign(project, defaultProject);
          emit('confirm');
          emit('update:dialogVisible', false);
        }
      })
    }

    const handleClose = (done: () => void) => {
      ElMessageBox.confirm('Are you sure to close this dialog?')
        .then((_) => {
          emit('update:dialogVisible', false);
          done();
        })
        .catch((_) => {})
    }

    return {
      dialogTitle,
      project,
      save,
      handleClose
    }
  }
})
</script>

<style scoped>
.dialog {
  max-width: 500px !important;
}
</style>
