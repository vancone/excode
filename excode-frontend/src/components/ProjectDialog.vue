<template>
  <el-dialog
    :title="dialogTitle"
    v-model="dialogVisible"
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
      <el-button size="small" @click="dialogVisible = false">Cancel</el-button>
      <el-button type="primary" size="small" @click="save">Save</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from 'vue'
import { ElMessageBox } from 'element-plus'
import { createProject, queryProject } from '~/api'
import { IProject } from '~/api/types'

const defaultProject: IProject = {
  id: '',
  name: '',
  version: '0.1.0-SNAPSHOT',
  description: '',
  author: '',
  organization: ''
}

export default defineComponent({
  name: 'ProjectDialog',
  setup () {
    const dialogTitle = ref('')
    const dialogVisible = ref(false)
    const callbackFunc = ref(null)
    const project = reactive<IProject>(defaultProject)

    const save = () => {
      createProject(project).then(({ data }) => {
        if (data.code === 0) {
          Object.assign(project, defaultProject);
          dialogVisible.value = false;
          callbackFunc.value();
        }
      })
    }

    const handleClose = (done) => {
      ElMessageBox.confirm('Are you sure to close this dialog?')
        .then((_) => {
          done()
          // this.dialogVisible = false
        })
        .catch((_) => {})
    }

    const show = (projectId: string, callback) => {
      dialogVisible.value = true
      callbackFunc.value = callback
      if (projectId === undefined) {
        dialogTitle.value = 'New Project'
        Object.assign(project, {
          version: '1.0.0-SNAPSHOT'
        })
      } else {
        dialogTitle.value = 'Project Info'
        queryProject(projectId).then(({ data }) => {
          Object.assign(project, data.data)
        })
      }
    }

    return {
      dialogTitle,
      dialogVisible,
      project,
      save,
      show,
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
