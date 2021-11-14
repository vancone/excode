<template>
  <el-dialog
    :title="dialogTitle"
    v-model="dialogVisible"
    class="dialog"
    :before-close="handleClose"
  >
    <el-form ref="formRef" :model="form" label-width="auto">
      <el-form-item label="Name">
        <el-input v-model="form.name" size="small"></el-input>
      </el-form-item>
      <el-form-item label="Version">
        <el-input v-model="form.version" size="small"></el-input>
      </el-form-item>
      <el-form-item label="Description">
        <el-input v-model="form.description" size="small"></el-input>
      </el-form-item>
      <el-form-item label="Author">
        <el-input v-model="form.author" size="small"></el-input>
      </el-form-item>
      <el-form-item label="Organization">
        <el-input v-model="form.organization" size="small"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button size="small" @click="dialogVisible = false">Cancel</el-button>
      <el-button type="primary" size="small" @click="save">Save</el-button>
    </template>
  </el-dialog>
</template>

<script>
import { defineComponent, reactive, ref } from 'vue'
import { ElPopconfirm } from 'element-plus'
import { createProject, queryProject } from '@/api/project'
export default defineComponent({
  name: 'ProjectDialog',
  setup () {
    const dialogTitle = ref('')
    const dialogVisible = ref(false)
    const callbackFunc = ref(null)
    const form = reactive({
      name: '',
      version: '',
      description: ''
    })

    const save = () => {
      createProject(form).then(({ data }) => {
        if (data.code === 0) {
          dialogVisible.value = false
          callbackFunc.value()
        }
      })
    }

    const handleClose = (done) => {
      ElPopconfirm('Are you sure to close this dialog?')
        .then((_) => {
          done()
          // this.dialogVisible = false
        })
        .catch((_) => {})
    }

    const show = (projectId, callback) => {
      dialogVisible.value = true
      callbackFunc.value = callback
      if (projectId === undefined) {
        dialogTitle.value = 'New Project'
        Object.assign(form, {
          version: '1.0.0-SNAPSHOT'
        })
      } else {
        dialogTitle.value = 'Project Info'
        queryProject(projectId).then(({ data }) => {
          Object.assign(form, data.data)
        })
      }
    }

    return {
      dialogTitle,
      dialogVisible,
      form,
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
