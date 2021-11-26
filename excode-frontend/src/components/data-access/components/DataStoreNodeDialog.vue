<template>
  <el-dialog
    :title="
      dataStoreNode.name === '' ? 'New Data Store Node' : 'Data Store Node'
    "
    :model-value="dialogVisible"
    class="dialog"
    :before-close="handleClose"
  >
    <el-form ref="formRef" :model="dataStoreNode" label-width="auto">
      <el-form-item label="Name">
        <el-input v-model="dataStoreNode.name" size="small"></el-input>
      </el-form-item>
      <el-form-item label="Type">
        <el-select v-model="dataStoreNode.type" size="small">
          <el-option label="VARCHAR" value="VARCHAR" />
        </el-select>
      </el-form-item>
      <el-form-item label="Length">
        <el-input v-model="dataStoreNode.length" size="small"></el-input>
      </el-form-item>
      <el-form-item label="Comment">
        <el-input v-model="dataStoreNode.comment" size="small"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button
        size="small"
        @click="$emit('update:dialogVisible', $event.target.value)"
        >Cancel</el-button
      >
      <el-button type="primary" size="small" @click="save">Save</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from "vue";
import { ElMessageBox } from "element-plus";
import { defaultDataStoreNode } from "~/api/default-value";
import { IDataStore, IDataStoreNode } from "~/api/types";
export default defineComponent({
  name: "DataStoreNodeDialog",
  props: {
    dialogVisible: Boolean,
    dataStoreNode: {
      type: Object,
      default: reactive<IDataStoreNode>({ ...defaultDataStoreNode }),
    },
  },
  setup(props, { emit }) {
    const dialogTitle = ref("");
    const dataStoreNode = props.dataStoreNode as IDataStore;

    function save() {
      emit("confirm", dataStoreNode);
      emit("update:dialogVisible", false);
    }

    function handleClose(done: () => void) {
      ElMessageBox.confirm("Are you sure to close this dialog?")
        .then((_) => {
          emit("update:dialogVisible", false);
          done();
        })
        .catch((_) => {});
    }

    return {
      dialogTitle,
      dataStoreNode,
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
