<template>
  <el-dialog
    :title="dataStore.id === ''? 'New Data Store': 'Data Store'"
    :model-value="dialogVisible"
    class="dialog"
    :before-close="handleClose"
  >
    <el-form ref="formRef" :model="dataStore" label-width="auto">
      <el-form-item label="Name">
        <el-input v-model="dataStore.name" size="small"></el-input>
      </el-form-item>
      <el-form-item label="Type">
        <el-select v-model="dataStore.type" size="small">
          <el-option label="Columnar" value="COLUMNAR" />
          <el-option label="Document" value="DOCUMENT" />
          <el-option label="Key-Value" value="KEY_VALUE" />
        </el-select>
      </el-form-item>
      <el-form-item label="Carrier">
        <el-select v-model="dataStore.carrier" size="small">
          <el-option-group label="Relational Database">
            <el-option label="MySQL" value="MYSQL" />
            <el-option label="PostgreSQL" value="POSTGRESQL" />
          </el-option-group>
          <el-option-group label="NoSQL Database">
            <el-option label="MongoDB" value="MONGODB" />
            <el-option label="Redis" value="REDIS" />
          </el-option-group>
          <el-option-group label="Message Broker">
            <el-option label="Kafka" value="KAFKA" />
            <el-option label="RabbitMQ" value="RABBITMQ" />
          </el-option-group>
        </el-select>
      </el-form-item>
      <el-form-item label="Description">
        <el-input v-model="dataStore.description" size="small"></el-input>
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
import { useRoute } from 'vue-router'
import { createDataStore, updateDataStore } from '~/api/index'
import { defaultDataStore } from '~/api/default-value'
import { IDataStore } from '~/api/types'
export default defineComponent({
  name: 'DataStoreDialog',
  props: {
    dialogVisible: Boolean,
    dataStore: {
      type: Object,
      default: reactive<IDataStore>({...defaultDataStore})
    }
  },
  setup(props, { emit }) {
    const route = useRoute()
    const dialogTitle = ref('')
    const dataStore = props.dataStore as IDataStore

    function save() {
      if (dataStore.id === '') {
        dataStore.projectId = route.params.projectId as string
        createDataStore(dataStore).then(({ data }) => {
          if (data.code === 0) {
            emit('confirm')
            emit('update:dialogVisible', false)
          }
        })
      } else {
        updateDataStore(dataStore).then(({ data }) => {
          if (data.code === 0) {
            emit('confirm')
            emit('update:dialogVisible', false)
          }
        })
      }
    }

    function handleClose(done: () => void) {
      ElMessageBox.confirm('Are you sure to close this dialog?')
        .then((_) => {
          emit('update:dialogVisible', false)
          done()
        })
        .catch((_) => {})
    }

    return {
      dialogTitle,
      dataStore,
      save,
      handleClose,
    };
  },
});
</script>

<style scoped>
.dialog {
  max-width: 500px !important;
}
</style>
