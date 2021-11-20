<template>
  <el-dialog
    :title="dialogTitle"
    v-model="dialogVisible"
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
        <el-select
          v-model="dataStore.carrier"
          size="small"
        >
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
      <el-button size="small" @click="dialogVisible = false">Cancel</el-button>
      <el-button type="primary" size="small" @click="save">Save</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from 'vue';
import { ElMessageBox } from 'element-plus';
import { createDataStore, queryDataStore, updateDataStore } from '~/api/index';
import { defaultDataStore } from '~/api/default-value';
import { useRoute } from 'vue-router';
import { IDataStore } from '~/api/types';
export default defineComponent({
  name: 'DataStoreDialog',
  setup() {
    const route = useRoute()
    const dialogTitle = ref('');
    const dialogVisible = ref(false);
    const callbackFunc = ref(null);
    const dataStore = reactive<IDataStore>({...defaultDataStore});

    function save() {
      console.log('before save', dataStore)
      if (dataStore.id === '') {
        createDataStore(dataStore).then(({ data }) => {
          if (data.code === 0) {
            dialogVisible.value = false;
            clear()
            callbackFunc.value();
          }
        });
      } else {
        updateDataStore(dataStore).then(({ data }) => {
          if (data.code === 0) {
            dialogVisible.value = false;
            clear()
            callbackFunc.value();
          }
        });
      }
    }

    function clear() {
      Object.assign(dataStore, defaultDataStore);
    }

    function handleClose(done) {
      ElMessageBox.confirm('Are you sure to close this dialog?')
        .then((_) => {
          clear();
          done();
        })
        .catch((_) => {});
    }

    function show(dataStoreId: string, callback: () => void) {
      dialogVisible.value = true;
      callbackFunc.value = callback;
      if (dataStoreId === null) {
        dialogTitle.value = 'New Data Store';
        Object.assign(dataStore, defaultDataStore);
        dataStore.projectId = route.params.projectId as string;
      } else {
        dialogTitle.value = 'Data Store';
        queryDataStore(dataStoreId).then(({ data }) => {
          Object.assign(dataStore, data.data);
        });
      }
    }

    return {
      dialogTitle,
      dialogVisible,
      dataStore,
      save,
      show,
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
