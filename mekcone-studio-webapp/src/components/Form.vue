<template>
  <div class="property-form">
    <a-form-model :layout="form.layout" :model="form" v-bind="formItemLayout">
    <a-form-model-item label="Field A">
      <a-input v-model="form.fieldA" placeholder="input placeholder" />
    </a-form-model-item>
    <a-form-model-item label="Field B">
      <a-input v-model="form.fieldB" placeholder="input placeholder" />
    </a-form-model-item>
    <a-form-model-item :wrapper-col="buttonItemLayout.wrapperCol">
      <a-button type="primary">
        Submit
      </a-button>
    </a-form-model-item>
  </a-form-model>
  </div>
</template>

<script>
// import { getProjectList } from '@/api/project'

export default {
  name: 'Project',
  components: {
  },
  data () {
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
      newTabIndex: 0,
      form: {
        layout: 'vertical',
        fieldA: '',
        fieldB: ''
      }
    }
  },
  computed: {
    /* formItemLayout() {
      const { layout } = this.form;
      return layout === 'horizontal'
        ? {
            labelCol: { span: 4 },
            wrapperCol: { span: 14 },
          }
        : {};
    },
    buttonItemLayout() {
      const { layout } = this.form;
      return layout === 'horizontal'
        ? {
            wrapperCol: { span: 14, offset: 4 },
          }
        : {};
    }, */
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
.property-form {
  color: #bbb;
}
.property-form table {
  margin-left: 15px;
  border: solid 1px #373737;
}
/deep/ .ant-input {
  background: #3c3f41;
  border-radius: 0;
  border: none;
  height: 30px;
  margin: 5px;
  color: #bbb;
}
.ant-tabs-content {
  height: 100%;
  width: 100%;
  overflow: scroll;
}
tr th {
  background: #3b3b3b;
}
/************** Tab **************/
/deep/ .ant-tabs-tab {
  height: 30px !important;
  width: 90px !important;
  background-color: #4e5254;
  border-radius: 0 !important;
  border: none !important;
  color: #bbb !important;
  font-size: 12px !important;
  margin: 0 !important;
  text-align: left !important;
  padding-left: 12px !important;
  padding-top: 6px !important;
}
/deep/ .ant-tabs-tab:hover {
  background: #2d2f30;
}
/deep/ .ant-tabs-bar {
  border-right: solid 1px #323232;
  background: #3c3f41;
  height: 100%;
}
/deep/ .ant-tabs.ant-tabs-card {
  background: #2b2b2b;
}
/deep/ .ant-tabs-tab-active {
  background: #333537 !important;
  border-bottom: none !important;
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
  color: #222;
  height: 15px !important;
  width: 15px !important;
  border-radius: 15px;
  background-color: #535a5e;
}
/deep/ .ant-tabs-right-content {
  padding-right: 0;
}
/************** Tree **************/
.left-panel {
  height: 100%;
  background: #3c3f41;
  border-right: solid 1px #323232;
  /* position: relative; */
  height:100%;
  width: 100%;
  margin-bottom:0;
}
.left-panel-header {
  text-align: left;
  background: #3b4754;
  border-bottom: solid 1px #323232;
  height: 30px;
  padding-top: 7px;
}
.left-panel-header h1 {
  position: relative;
  left: 10px;
  font-size: 12px;
  color: #bbb;
}
.left-panel-toolbar {
  text-align: left;
  background: transparent;
  border-bottom: solid 1px #323232;
  height: 30px;
  padding-top: 7px;
}
/deep/ .ant-tree {
  padding: 0 !important;
  width: 100%;
  /* border-left: solid 1px #323232; */
}
/deep/ .module-tree .ant-tree-title {
  color: #bbb;
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
/deep/ .ant-dropdown-link {
  position: relative;
  top: -13px;
  height: 25px !important;
  width: 30px !important;
}
/deep/ .ant-dropdown ul {
  background: yellow !important;
}
/deep/ .ant-dropdown-link:hover {
  background: #4c5052 !important;
}
/deep/ .ant-dropdown-menu-vertical {
  background: tomato !important;
}
/deep/ .ant-dropdown-menu {
  background: tomato !important;
}
/deep/ .ant-dropdown-menu-root {
  background: tomato !important;
}
/deep/ .ant-dropdown-content {
  background: tomato !important;
}
/deep/ .ant-dropdown-menu-item {
  background: #3c3f41;
  color: #bbb;
}
/deep/ .ant-dropdown-menu-item:hover {
  background: #4b6eaf;
  color: #bbb;
}
/deep/ .ant-dropdown-menu-item-divider {
  background: #515151;
}
</style>
