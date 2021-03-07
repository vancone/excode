<template>
  <div class="left-panel">
    <a-tabs
      default-active-key="mod"
      tab-position="left"
      class="tabs"
      @prevClick="callback"
      @nextClick="callback"
    >
      <a-tab-pane tab="Module" key="mod">
        <div class="left-panel-header">
          <h1>Modules</h1>
        </div>
        <div class="left-panel-toolbar">
          <a-dropdown :trigger="['hover']">
            <a class="ant-dropdown-link" style="color:#bbb;font-size:26px;" @click="e => e.preventDefault()">
              &nbsp;+&nbsp;<a-icon type="caret-down" style="font-size:9px;position:relative;left:-14px;top:4px;"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item key="0">
                <a href="http://www.alipay.com/">Front-End</a>
              </a-menu-item>
              <a-menu-item key="1">
                <a href="http://www.taobao.com/">Back-End</a>
              </a-menu-item>
              <a-menu-divider />
              <a-menu-item key="3">
                More...
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </div>
        <a-tree
          show-line
          class="module-tree"
          style="background:#3c3f41;height:inherit;padding:10px;color:#bbb;font-size:12px;"
          :tree-data="treeData"
          :default-expanded-keys="['0-0-0', '0-0-1']"
          :default-selected-keys="['0-0-0', '0-0-1']"
          :default-checked-keys="['0-0-0', '0-0-1']"
          @select="onSelect"
        >
          <a-icon slot="switcherIcon" type="down" />
          <span slot="title0010" style="color: #1890ff">sss</span>
        </a-tree>
      </a-tab-pane>
      <a-tab-pane tab="Data Source" key="ds">
        <div class="left-panel-header">
          <h1>Data Source</h1>
        </div>
        <div class="left-panel-toolbar">
          <a-dropdown :trigger="['hover']">
            <a class="ant-dropdown-link" style="color:#bbb;font-size:26px;" @click="e => e.preventDefault()">
              &nbsp;+&nbsp;<a-icon type="caret-down" style="font-size:9px;position:relative;left:-14px;top:4px;"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item key="0">
                <img class="data-source-logo" src="../assets/mysql.svg">
                <a style="display:inline-block" @click="createDataSource('mysql')">MySQL</a>
              </a-menu-item>
              <a-menu-divider />
              <a-menu-item key="1">
                <img class="data-source-logo" src="../assets/mongodb.svg">
                <a style="display:inline-block" @click="createDataSource('mongodb')">MongoDB</a>
              </a-menu-item>
              <a-menu-item key="2">
                <img class="data-source-logo" src="../assets/redis.svg">
                <a style="display:inline-block" @click="createDataSource('redis')">Redis</a>
              </a-menu-item>
              <a-menu-divider />
              <a-menu-item key="3">
                More...
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </div>
        <a-tree
          show-line
          class="module-tree"
          style="background:#3c3f41;height:inherit;padding:10px;color:#bbb;font-size:12px;"
          :tree-data="treeData2"
          :default-expanded-keys="['0-0-0', '0-0-1']"
          :default-selected-keys="['0-0-0', '0-0-1']"
          :default-checked-keys="['0-0-0', '0-0-1']"
          @select="onSelect"
        >
          <a-icon slot="switcherIcon" type="down" />
          <span slot="title0010" style="color: #1890ff">sss</span>
        </a-tree>
      </a-tab-pane>
      <a-tab-pane tab="Code" key="code"></a-tab-pane>
    </a-tabs>
  </div>
</template>

<script>
// import { getProjectList } from '@/api/project'

export default {
  name: 'Project',
  components: {
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
      treeData2: [
        {
          title: 'MongoDB',
          key: 'mongodb'
        },
        {
          title: 'MySQL',
          key: 'parent',
          children: [
            {

            }
          ]
        },
        {
          title: 'Redis',
          key: 'redis'
        }
      ],
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
    },
    createDataSource (type) {
      alert(type)
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
.tabs {
  height: 100% !important;
  width: 100%;
}
.ant-tabs-content {
  height: 100%;
  width: 100%;
  overflow: scroll;
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
.ant-dropdown-menu-item > a {
  color: #bbb;
}
/deep/ .ant-dropdown-placement-bottomLeft {
  background: red !important;
}
.ant-dropdown-menu {
  background-color:transparent !important;
}
.data-source-logo {
  height: 15px;
  width: 15px;
}
.ant-dropdown-menu-item {
  font-size: 12px;
}
</style>
