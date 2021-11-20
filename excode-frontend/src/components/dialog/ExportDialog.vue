<template>
  <div class="project-dialog">
    <!-- Create Project Dialog -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="50%" :before-close="handleClose">
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item label="Project name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item>
          <el-checkbox label="Spring Boot" name="type"></el-checkbox>
          <el-checkbox label="Vue.js" name="type"></el-checkbox>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="save">Save</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import axios from 'axios'
export default {
  name: 'ExportDialog',
  data () {
    return {
      dialogVisible: false,
      dialogTitle: ''
    }
  },
  methods: {
    save () {
      const _this = this
      axios.post('/api/excode/project', this.form)
        .then((res) => {
          _this.$message({
            message: 'Project saved.',
            type: 'success'
          })
          _this.dialogVisible = false
          _this.form = {}
          _this.refresh()
        })
        .catch((err) => {
          console.log(err)
        })
    },
    handleClose (done) {
      this.$confirm('Are you sure to close this dialog?')
        .then(_ => {
          done()
          this.dialogVisible = false
        })
        .catch(_ => {})
    }
  },
  mounted: function () {
  }
}
</script>

<style scoped>
</style>
