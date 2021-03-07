<template>
  <div class="project-page">
    <!-- <GlobalHeader style="position:relative;z-index:2000;"/> -->
    <a-page-header
      class="page-header"
      :title="projectName"
      @back="() => $router.go(-1)"
    >
    </a-page-header>

    <tool-bar></tool-bar>

    <a-row style="height:calc(100% - 65px);">
      <a-col :xs="10" :sm="6" :lg="5" style="text-align:left;height:100%;">
        <left-panel></left-panel>
      </a-col>
      <a-col :xs="14" :sm="18" :lg="19" style="text-align:left;height:100%;">
        <a-tabs v-model="activeKey" hide-add type="editable-card" @edit="onEdit" style="height:100%">
          <a-tab-pane key="projectProperties" tab="Project" closable="closable">
            <project-properties></project-properties>
          </a-tab-pane>
          <a-tab-pane key="propertyForm" tab="Properties" closable="closable">
            <property-form></property-form>
            <relational-database></relational-database>
          </a-tab-pane>
        </a-tabs>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import GlobalHeader from '@/components/GlobalHeader'
import ToolBar from '@/components/ToolBar'
import LeftPanel from '@/components/LeftPanel'
import PropertyForm from '@/components/Form'
import ProjectProperties from '@/components/ProjectProperties'
import RelationalDatabase from '@/components/RelationalDatabase'
// import { getProjectList } from '@/api/project'

export default {
  name: 'Project',
  components: {
    GlobalHeader, ProjectProperties, LeftPanel, PropertyForm, ToolBar, RelationalDatabase
  },
  data () {
    const panes = [
      { title: 'Tab 1', content: 'Content of Tab 1', key: '1' },
      { title: 'Tab 2', content: 'Content of Tab 2', key: '2' }
    ]
    return {
      projectName: '',
      treeData: [{
        title: '',
        key: 'parent',
        children: [
          {

          }
        ]
      }],
      activeKey: 'projectProperties',
      panes,
      newTabIndex: 0
    }
  },
  created () {
    /* getProjectList().then((res) => {
      this.data = res.data.content
    }) */
  },
  methods: {
    /* loadProject (project) {
      this.$store.commit('id', project.id)
      this.$store.commit('name', project.name)
      this.$store.commit('modules', project.modules)
      this.$router.push('/project')
    }, */
    onSelect (selectedKeys, info) {
      // console.log('selected', selectedKeys, info)
      console.log(selectedKeys)
    },
    loadTree () {
      this.treeData[0].title = this.$store.getters.name
      this.treeData[0].children = this.$store.getters.modules.map(element => {
        return {
          title: element.name,
          key: 'mod-' + element.id,
          children: element.extensions.map(subElement => {
            return {
              title: subElement.name,
              key: 'ext-' + subElement.id
            }
          })
        }
      })
    },
    callback (key) {
      console.log(key)
    },
    onEdit (targetKey, action) {
      this[action](targetKey)
    },
    add () {
      const panes = this.panes
      const activeKey = `newTab${this.newTabIndex++}`
      panes.push({
        title: `New Tab ${activeKey}`,
        content: `Content of new Tab ${activeKey}`,
        key: activeKey
      })
      this.panes = panes
      this.activeKey = activeKey
    },
    remove (targetKey) {
      let activeKey = this.activeKey
      let lastIndex
      this.panes.forEach((pane, i) => {
        if (pane.key === targetKey) {
          lastIndex = i - 1
        }
      })
      const panes = this.panes.filter(pane => pane.key !== targetKey)
      if (panes.length && activeKey === targetKey) {
        if (lastIndex >= 0) {
          activeKey = panes[lastIndex].key
        } else {
          activeKey = panes[0].key
        }
      }
      this.panes = panes
      this.activeKey = activeKey
    }
  },
  mounted () {
    if (this.$store.getters.id !== '') {
      this.loadTree()
      this.projectName = this.$store.getters.name
    } else {
      this.$router.push('/')
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.project-page {
  height:100%;
  margin-bottom:0;
  overflow: hidden;
}
.page-header {
  border: 1px solid #515151;
  background:#3c3f41;
}
.ant-tabs-content {
  height: 100%;
  overflow: scroll;
  background-color: white;
}
/deep/ .ant-page-header-heading-sub-title {
  color: #bbb;
}
/deep/ .ant-page-header {
  height: 50px;
  padding: 8px 16px;
}
/deep/ .ant-page-header-heading-title {
  font-weight: 400;
  font-size: 16px;
}
/************** Tab **************/
/deep/ .ant-tabs-left-content {
  padding-left: 0 !important;
}
/deep/ .ant-tabs-bar {
  border-bottom: solid 1px #323232;
  background: #3c3f41;
  height: 30px;
}
/deep/ .ant-tabs.ant-tabs-card {
  background: #2b2b2b;
}
/deep/ .ant-tabs-tab {
  padding: 0 !important;
  background-color: #3c3f41 !important;
  border-radius: 0 !important;
  border: none !important;
  color: #bbb !important;
  height: 30px !important;
  font-size: 12px !important;
  padding-left: 10px !important;
  padding-right: 15px !important;
}
/deep/ .ant-tabs-tab:hover {
  background: #27292a !important;
}
/deep/ .ant-tabs-tab div {
  margin-top: -5px !important;
}
/deep/ .ant-tabs-tab-active {
  background: #4e5254 !important;
  border: none;
  border-bottom: solid 3px #4a88c7 !important;
  color: #bbb;
}
/deep/ .ant-tabs-tab-active:hover {
  background: #333537 !important;
}
/deep/ .ant-tabs-close-x {
  color: #666 !important;
  margin-left: 10px !important;
  margin-right: -8px !important;
  margin-top: -3px !important;
  padding-top: 1px !important;
}
/deep/ .ant-tabs-close-x:hover {
  color: #222 !important;
  height: 15px !important;
  width: 15px !important;
  border-radius: 15px !important;
  background-color: #535a5e !important;
}
/************** Tree **************/
.right-panel {
  height: 100%;
  background: #3c3f41;
  border-left: solid 1px #323232;
  position: relative;
  /* z-index: 10000; */
}
.right-panel-header {
  text-align: left;
  background: #3b4754;
  border-bottom: solid 1px #323232;
  height: 30px;
  padding-top: 7px;
}
.right-panel-header h1 {
  position: relative;
  left: 10px;
  font-size: 12px;
  color: #bbb;
}
.right-panel-toolbar {
  text-align: left;
  background: transparent;
  border-bottom: solid 1px #323232;
  height: 330px;
  padding-top: 7px;
}
/deep/ .ant-tree {
  padding: 0 !important;
  /* border-left: solid 1px #323232; */
}
/deep/ .ant-tree-switcher {
  background: transparent !important;
  color: #adadad !important;
}
/deep/ .ant-tree-node-content-wrapper:hover {
  background: #0d293e !important;
}
/* /deep/ .ant-tree-node-content-wrapper {
  background: transparent !important;
} */
/deep/ .ant-tree-node-selected {
  background: #4b6eaf !important;
}

</style>
