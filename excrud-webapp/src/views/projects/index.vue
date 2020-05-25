<template>
  <div class="app-container">
    <div class="filter-container">
      <el-dropdown class="filter-item">
            <el-button type="primary">
              +&nbsp;&nbsp;New<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleCreate">Create Project</el-dropdown-item>
              <el-dropdown-item divided @click.native="$refs.file.click()">Import</el-dropdown-item>
            </el-dropdown-menu>
      </el-dropdown>
      <input type="file" ref="file" id="file" accept=".xml,.json" style="display:none" @change="importProject($event)">
      <el-input v-model="listQuery.name" placeholder="Name" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.importance" placeholder="Imp" clearable style="width: 90px" class="filter-item">
        <el-option v-for="item in importanceOptions" :key="item" :label="item" :value="item" />
      </el-select>
      <el-select v-model="listQuery.type" placeholder="Type" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in calendarTypeOptions" :key="item.key" :label="item.display_name+'('+item.key+')'" :value="item.key" />
      </el-select>
      <el-select v-model="listQuery.sort" style="width: 140px" class="filter-item" @change="handleFilter">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        Search
      </el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      stripe
      fit
      highlight-current-row
      style="width: 100%;box-shadow:0px 2px 5px #ddd;font-size:13px"
      @sort-change="sortChange"
    >
      <!-- <el-table-column label="ID" prop="id" sortable="custom" align="center" width="80" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column> -->
      <!-- <el-table-column label="Date" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.timestamp | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column> -->
      <el-table-column
      type="selection"
      width="35">
    </el-table-column>
     <el-table-column label="Basic Information">
      <el-table-column label="ID" width="130px">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.groupId }}</span><br>
          <span class="link-type" @click="handleUpdate(row)">{{ row.artifactId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Version" width="70px">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.version }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Name" min-width="60px">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.name }}</span>
          <!-- <el-tag>{{ row.type | typeFilter }}</el-tag> -->
        </template>
      </el-table-column>
      <el-table-column label="Description" min-width="120px">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.description }}</span>
          <!-- <el-tag>{{ row.type | typeFilter }}</el-tag> -->
        </template>
      </el-table-column>
      <el-table-column label="Updated Time" width="110px">
        <template slot-scope="{row}">
          <span>{{ row.updatedDate }}</span><br>
          <span>{{ row.updatedTime }}</span>
        </template>
      </el-table-column>
      </el-table-column>
      <!-- <el-table-column label="Author" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.author }}</span>
        </template> -->
      <!-- </el-table-column>
      <el-table-column v-if="showReviewer" label="Reviewer" width="110px" align="center">
        <template slot-scope="{row}">
          <span style="color:red;">{{ row.reviewer }}</span>
        </template> -->
      <!-- </el-table-column>
      <el-table-column label="Imp" width="80px">
        <template slot-scope="{row}">
          <svg-icon v-for="n in + row.importance" :key="n" icon-class="star" class="meta-item__icon" />
        </template>
      </el-table-column>
      <el-table-column label="Readings" align="center" width="95">
        <template slot-scope="{row}">
          <span v-if="row.pageviews" class="link-type" @click="handleFetchPv(row.pageviews)">{{ row.pageviews }}</span>
          <span v-else>0</span>
        </template>
      </el-table-column> -->
      <el-table-column label="Modules" width="110">
        <template slot-scope="{row}">
           <el-dropdown>
            <el-button type="primary" size="mini" style="background:#fff;border:solid 1px #ddd;color:black;">
              Modules<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>Add Module...</el-dropdown-item>
              <el-dropdown-item divided>API Document</el-dropdown-item>
              <router-link to="/servers"><el-dropdown-item>Relational Database</el-dropdown-item></router-link>
              <el-dropdown-item>Spring Boot</el-dropdown-item>
              <el-dropdown-item>Vue Element Admin</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <!-- <el-tag :type="row.status | statusFilter">
            {{ row.status }}
          </el-tag> -->
          
          <!-- <el-tag>API Doc</el-tag>
          <el-tag>Relational Database</el-tag>
          <el-tag>Spring Boot</el-tag>
          <el-tag>Vue Element Admin</el-tag> -->
        </template>
      </el-table-column>
      <el-table-column label="Deployment">
        <el-table-column label="Host" width="120px">
        <template slot-scope="{row}">
          <span>127.0.0.1</span><br><span>192.168.255.250</span>
        </template>
      </el-table-column>
      <el-table-column label="Deploy" width="90">
        <template slot-scope="{row,$index}">
          <el-button v-if="row.status!='deployed'" size="mini" type="primary" @click="handleModifyStatus(row,'deployed')">
            Deploy
          </el-button>
          <el-tooltip class="item" effect="dark" content="Deployed at 15:07:39, 2020-05-19" placement="bottom">
            <span v-if="row.status=='deployed'" style="color:green">Deployed</span>
          </el-tooltip>
          
        </template>
      </el-table-column>
      <el-table-column label="Status" width="110">
        <template slot-scope="{row,$index}">
          <el-dropdown>
            <el-button type="primary" size="mini" style="background:#fff;border:solid 1px #ddd;color:black;">
              Running<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>Run</el-dropdown-item>
              <el-dropdown-item>Stop</el-dropdown-item>
              <el-dropdown-item>Restart</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          
        </template>
      </el-table-column>
       </el-table-column>
      <el-table-column label="Actions" width="100" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-dropdown>
            <el-button type="primary" size="mini" style="background:purple;border:purple">
              Export<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="exportProject('xml', row.id)">ExCRUD Project File</el-dropdown-item>
              <el-dropdown-item>Source Code (Premium only)</el-dropdown-item>
              <el-dropdown-item>Built Packages</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown><br/>
          <el-button v-if="row.status!='deleted'" style="margin-top:5px;" size="mini" type="danger" @click="handleDelete(row,$index)">
            Delete
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination style="height:50px;margin-top:15px;padding-top:10px;box-shadow:0px 2px 5px #ddd;" v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <!-- <el-form-item label="Type" prop="type">
          <el-select v-model="temp.type" class="filter-item" placeholder="Please select">
            <el-option v-for="item in calendarTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item> -->
        <!-- <el-form-item label="Date" prop="timestamp">
          <el-date-picker v-model="temp.timestamp" type="datetime" placeholder="Please pick a date" />
        </el-form-item> -->
        <el-form-item label="Group ID" prop="groupId" label-width="100px">
          <el-input v-model="temp.groupId" />
        </el-form-item>
        <el-form-item label="Artifact ID" prop="artifactId" label-width="100px">
          <el-input v-model="temp.artifactId" />
        </el-form-item>
        <el-form-item label="Version" prop="version" label-width="100px">
          <el-input v-model="temp.version" />
        </el-form-item>
        <el-form-item label="Name" prop="name" label-width="100px">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="Description" prop="description" label-width="100px">
          <el-input v-model="temp.description" />
        </el-form-item>
        <!-- <el-form-item label="Status">
          <el-select v-model="temp.status" class="filter-item" placeholder="Please select">
            <el-option v-for="item in statusOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item> -->
        <!-- <el-form-item label="Imp">
          <el-rate v-model="temp.importance" :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="3" style="margin-top:8px;" />
        </el-form-item> -->
        <!-- <el-form-item label="Remark">
          <el-input v-model="temp.remark" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="Please input" />
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          Cancel
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          Confirm
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="key" label="Channel" />
        <el-table-column prop="pv" label="Pv" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogPvVisible = false">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, uploadProject, fetchPv, createArticle, updateArticle, deleteProject } from '@/api/project'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import axios from 'axios'

const calendarTypeOptions = [
  { key: 'CN', display_name: 'China' },
  { key: 'US', display_name: 'USA' },
  { key: 'JP', display_name: 'Japan' },
  { key: 'EU', display_name: 'Eurozone' }
]

// arr to obj, such as { CN : "China", US : "USA" }
const calendarTypeKeyValue = calendarTypeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        importance: undefined,
        title: undefined,
        type: undefined,
        sort: '+id'
      },
      importanceOptions: [1, 2, 3],
      calendarTypeOptions,
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      temp: {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        type: '',
        status: 'published'
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        title: [{ required: true, message: 'title is required', trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        let responseData = response.data.map(element => {
          //element.updatedTime = '2010-01-01<br>\n19:30:20'
          element.updatedDate = element.updatedTime.substr(0,10)
          element.updatedTime = element.updatedTime.substr(11, 8)
          return element
        })
        this.list = response.data
        this.total = Math.max(0, response.data.length - 1)
        this.total = response.data.length //response.data.total
        this.listLoading = false

        // Just to simulate the time of the request
        /* setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000) */
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作Success',
        type: 'success'
      })
      row.status = status
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    importProject(event) {
      var file = event.target.files[0]
      var formData = new FormData()
      formData.append('file', file)
      var config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      axios.post('/api/excrud/project/import', formData, config).then( res => {
        if (res.data.code == 0) {
          this.$notify({
            title: 'Success',
            message: 'Import project successfully',
            type: 'success'
          });
          this.getList()
        } else {
          this.$notify.error({
          title: 'Error',
          message: 'Import project failed'
        });
        }
        
      }).catch( res => {
        this.$notify.error({
          title: 'Error',
          message: 'Upload file failed'
        });
      })

    },
    sortByID(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+id'
      } else {
        this.listQuery.sort = '-id'
      }
      this.handleFilter()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        status: 'published',
        type: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    exportProject(type, id) {
      let config = {responseType: 'blob'}
      let url = '/api/excrud/project/download/' + type + '/' + id
      axios.get(url, config).then( data => {
        if (!data) {
          this.$notify({
            title: 'Success',
            message: 'Export project successfully',
            type: 'success'
          });
        } else {
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', id + '.' + type)
          document.body.appendChild(link)
          link.click()
          this.$notify({
            title: 'Success',
            message: 'Export project successfully',
            type: 'success'
          });
          document.body.removeChild(link)
        }
      }).catch( res => {
        this.$notify.error({
          title: 'Error',
          message: 'Download file failed: ' + res
        });
      })
    },

    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
          this.temp.author = 'vue-element-admin'
          createArticle(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Created Successfully',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          updateArticle(tempData).then(() => {
            const index = this.list.findIndex(v => v.id === this.temp.id)
            this.list.splice(index, 1, this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Update Successfully',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row, index) {
      deleteProject(row.id).then(response => {
        if (response.code == 0) {
          this.$notify({
            title: 'Success',
            message: 'Delete Successfully',
            type: 'success',
            duration: 2000
          })
          this.getList()
        } else {
          this.$notify({
            title: 'Fail',
            message: 'Delete failed',
            type: 'error',
            duration: 2000
          })
        }
      })
      
      //this.list.splice(index, 1)

    },
    handleFetchPv(pv) {
      fetchPv(pv).then(response => {
        this.pvData = response.data.pvData
        this.dialogPvVisible = true
      })
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    }
  }
}
</script>
