<template>
  <el-dialog
    :title="application.id === ''? 'New Application': 'Application'"
    :model-value="dialogVisible"
    :before-close="handleClose"
    width="500px"
  >
    <el-form ref="formRef" :model="application" label-width="auto">
      <el-form-item label="App ID">
        <el-input v-model="application.id" size="small"></el-input>
      </el-form-item>
      <el-form-item label="Name">
        <el-input v-model="application.name" size="small"></el-input>
      </el-form-item>
      <el-form-item label="Owner">
        <el-input v-model="application.owner" size="small"></el-input>
      </el-form-item>
      <el-form-item label="Organization">
        <el-input v-model="application.organization" size="small"></el-input>
      </el-form-item>
      <el-form-item label="Description">
        <el-input v-model="application.description" size="small"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button size="small" @click="$emit('update:dialogVisible', false)">Cancel</el-button>
      <el-button type="primary" size="small" @click="save">Save</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from 'vue'
import { ElMessageBox } from 'element-plus'
import { createApplication } from '~/api'
import { IApplication } from '~/api/types'
import { defaultApplication } from '~/api/default-value'

export default defineComponent({
  name: 'ApplicationDialog',
  props: {
    dialogVisible: Boolean,
    application: {
      type: Object,
      default: reactive<IApplication>({...defaultApplication})
    }
  },
  setup (props, { emit }) {
    const dialogTitle = ref('New Application');

    const save = () => {
      const application = props.application as IApplication;
      createApplication(application).then(({ data }) => {
        if (data.code === 0) {
          Object.assign(application, defaultApplication);
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
      save,
      handleClose
    }
  }
})
</script>

<style scoped>
</style>
