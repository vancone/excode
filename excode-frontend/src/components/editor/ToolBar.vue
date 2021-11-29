<template>
    <div class="toolbar">
      <button class="button-back" @click="back"></button>
      <button class="button-save" :class="{disabled: editted}" @click="save" :disabled="editted">Save</button>
      <button class="button-datasource" @click="openDataSourceDialog">Data Source</button>
      <router-link to="/editor/raw"><button class="button-raw-code">Raw</button></router-link>
      <button class="button-settings" @click="openDataSourceDialog">Settings</button>
      <button class="button-export" @click="openDataSourceDialog">Export</button>
    </div>
</template>

<script lang="ts">
import axios from 'axios'
import { defineComponent } from 'vue-demi'
import { getProject } from '~/api/store'
import { editted } from '~/api/store'
export default defineComponent({
  name: 'EditorToolBar',
  components: {},
  setup() {
    return {
      editted
    }
  },
  data () {
    return {
      dataTableDialogVisible: false,
      dataSourceDialogVisible: false,
      dataTableKeyDialogVisible: false,
      dataTableKeySelectDataSourceDialogVisible: false,
      defaultProps: {
        children: 'nodes',
        label: 'value'
      },
      dataObject: {
        value: '',
        type: 'MYSQL',
        name: ''
      },
      dataSourceOptions: [],
      dataTableKey: {}
    }
  },
  methods: {
    save () {
      alert(JSON.stringify(getProject()))
    },
    back () {
      this.$router.push('/')
    },
    getUrlParam (name) {
      var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)')
      var r = window.location.search.substr(1).match(reg)
      if (r != null) return unescape(r[2])
      return null
    },
    toggleDataSourceType () {
      console.log('toggle data source type')
      const _this = this
      axios.get('/api/excode/data-source?type=' + this.dataObject.type)
        .then((res) => {
          _this.dataSourceOptions = res.data.data.content
          _this.dataObject.name = ''
        })
        .catch((err) => {
          console.log(err)
        })
    },
    openDataTableDialog () {
      this.toggleDataSourceType()
      this.dataTableDialogVisible = true
    },
    openDataSourceDialog () {
      this.dataSourceDialogVisible = true
    },
    openSelectDataSourceDialog () {
      this.dataTableKeySelectDataSourceDialogVisible = true
    },
    editDataTableKey (data) {
      this.dataTableKey = data
      if (this.dataTableKey.root === true) {
        this.dataTableKey.type = this.dataTableKey.dataSource.type
        if (this.dataTableKey.type === 'MYSQL') {
          this.dataTableKey.source = 'mysql://' + this.dataTableKey.dataSource.host + ':' + this.dataTableKey.dataSource.port + '/' + this.dataTableKey.dataSource.database + '?username=' + this.dataTableKey.dataSource.username
        }
      }
      this.dataTableKeyDialogVisible = true
    }
  }
})
</script>

<style scoped>
.toolbar {
  height: 35px;
  width: 100%;
  background: white;
  border-bottom: solid 1px #ddd;
  display: flex;
}
button {
  height: 35px;
  width: auto;
  cursor: pointer;
  margin-top: 0px;
  padding: 0 8px 0 8px;
  background-color: transparent;
  background-size: 16px 16px;
  background-repeat: no-repeat;
  background-position: 10px center;
  padding-left: 32px;
  color: #999;
  font-size: 12px;
  line-height: 35px;
  border: none;
  outline: none;
}
button:hover {
  background-color: #eee;
  color: #8ba74f;
}
.button-back {
  background-image: url(../../assets/back.svg);
}
.button-save {
  background-image: url(../../assets/save.svg);
}
.button-datasource {
  background-image: url(../../assets/database.svg);
}
.button-raw-code {
  background-image: url(../../assets/code.svg);
}
.button-settings {
  background-image: url(../../assets/settings.svg);
}
.button-export {
  background-image: url(../../assets/export.svg);
}
.disabled {
  background-image: url(../../assets/save-disabled.svg);
  color: #bbb;
}
</style>
