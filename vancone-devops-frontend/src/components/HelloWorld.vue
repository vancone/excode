<template>
  <div class="hello">
    <div class="toolbar">
      <el-button type="primary" @click="create"><i class="el-icon-plus" style="margin-right:10px;"></i>Create project</el-button>
    </div>
    <el-table :data="tableData" style="width:calc(100% - 40px);margin:20px">
      <el-table-column label="Project Name" width="180">
        <template #default="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Name" width="180">
        <template #default="scope">
          <el-popover effect="light" trigger="hover" placement="top">
            <template #default>
              <!-- <p>姓名: {{ scope.row.name }}</p>
              <p>住址: {{ scope.row.address }}</p> -->
            </template>
            <template #reference>
              <div class="name-wrapper">
                <el-tag size="medium">{{ scope.row.name }}</el-tag>
              </div>
            </template>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="Last Modified" width="180">
        <template #default="scope">
          <span>{{ scope.row.modifiedTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Operations">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.$index, scope.row)"><i class="el-icon-edit"></i>&nbsp;Edit</el-button>
          <el-button size="mini" @click="handleEdit(scope.$index, scope.row)"><i class="el-icon-setting"></i>&nbsp;Configure</el-button>
          <el-popconfirm title="Are you sure to delete this?" @confirm="handleDelete(scope.$index, scope.row)">
            <template #reference>
              <el-button size="mini" type="danger"><i class="el-icon-delete"></i>&nbsp;Delete</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- Create Project Dialog -->
    <el-dialog title="Create Project" v-model="dialogVisible" width="50%" :before-close="handleClose">
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item label="Project name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <!-- <el-form-item label="Activity zone">
          <el-select v-model="form.region" placeholder="please select your zone">
            <el-option label="Zone one" value="shanghai"></el-option>
            <el-option label="Zone two" value="beijing"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Activity time">
          <el-col :span="11">
            <el-date-picker type="date" placeholder="Pick a date" v-model="form.date1" style="width: 100%;"></el-date-picker>
          </el-col>
          <el-col class="line" :span="2">-</el-col>
          <el-col :span="11">
            <el-time-picker placeholder="Pick a time" v-model="form.date2" style="width: 100%;"></el-time-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="Instant delivery">
          <el-switch v-model="form.delivery"></el-switch>
        </el-form-item>
        <el-form-item label="Activity type">
          <el-checkbox-group v-model="form.type">
            <el-checkbox label="Online activities" name="type"></el-checkbox>
            <el-checkbox label="Promotion activities" name="type"></el-checkbox>
            <el-checkbox label="Offline activities" name="type"></el-checkbox>
            <el-checkbox label="Simple brand exposure" name="type"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="Resources">
          <el-radio-group v-model="form.resource">
            <el-radio label="Sponsor"></el-radio>
            <el-radio label="Venue"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Activity form">
          <el-input type="textarea" v-model="form.desc"></el-input>
        </el-form-item> -->
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="save">Save</el-button>
        </span>
      </template>
    </el-dialog>
    <div class="footer">
      <p>ExCRUD Low-Code Project Build Platform</p>
      <p>&copy; 2020-2021 VanCone. All rights reserved.</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'HelloWorld',
  props: {
    msg: String
  },
  data () {
    return {
      tableData: [],
      dialogVisible: false,
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      }
    }
  },
  methods: {
    refresh () {
      const _this = this
      axios.get('/api/devops/project')
        .then((res) => {
          _this.tableData = res.data.data
          console.log(res.data)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    create () {
      this.form = {}
      this.dialogVisible = true
    },
    save () {
      const _this = this
      axios.post('/api/devops/project', this.form)
        .then((res) => {
          _this.$message('Save project success')
          _this.dialogVisible = false
          _this.form = {}
          _this.refresh()
          console.log(res.data)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    handleEdit (index, row) {
      console.log(index, row)
      this.form = row
      this.dialogVisible = true
    },
    handleDelete (index, row) {
      const _this = this
      axios.delete('/api/devops/project/' + row.id)
        .then((res) => {
          _this.$message('Operation Success')
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
    this.refresh()
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
.toolbar {
  margin-top: 20px;
  margin-left: 20px;
  text-align: left;
}
.footer {
  color: #999;
  position: fixed;
  font-size: 14px;
  bottom: 0;
  width: 100%;
}
.footer p {
  margin: 5px;
}
</style>
