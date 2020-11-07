<template>
  <div class="right-panel">
    <a-tabs
      default-active-key="mod"
      tab-position="right"
      class="tabs"
      @prevClick="callback"
      @nextClick="callback"
    >
      <a-tab-pane tab="Module" key="mod">
        <div class="right-panel-header">
          <h1>Modules</h1>
        </div>
        <div class="right-panel-toolbar">
          <a-dropdown :trigger="['hover']">
            <a class="ant-dropdown-link" @click="e => e.preventDefault()">
              &nbsp;+&nbsp;<a-icon type="down" />
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
          style="background:#3c3f41;height:inherit;padding:10px;color:#bbb;"
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
      <a-tab-pane tab="Data Source" key="ds"></a-tab-pane>
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
  /* background: #1f2022; */
  height: 30px !important;
  width: 90px !important;
  /* position: relative;
  top: 10px; */
  transform: rotate(90deg);
  background-color: #4e5254;
  border-radius: 0 !important;
  border: none !important;
  color: #bbb !important;
  font-size: 12px !important;
  padding-top: 45px !important;
  padding-bottom: 45px !important;
  padding-left: 10px !important;
  padding-right: 10px !important;
  position: relative;
  left: -20px;
  margin: 0 !important;
}
/deep/ .ant-tabs-tab:hover {
  background: #2d2f30;
}
/deep/ .ant-tabs-nav {
  /* height: 100px; */
}
/deep/ .ant-tabs-bar {
  border-left: solid 1px #323232;
  background: #3c3f41;
  height: 100%;
  width: 30px;
  /* overflow: hidden; */
}
/deep/ .ant-tabs.ant-tabs-card {
  background: #2b2b2b;
}
/deep/ .ant-tabs-tab div {
  /* width: 102px; */
  /* margin-top: -5px !important; */
}
/deep/ .ant-tabs-tab-active {
  background: #333537 !important;
  /* border: none !important; */
  /* border-bottom: solid 3px #4a88c7 !important; */
  color: #bbb;
  /* height: 200px; */
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
/deep/ .ant-tabs-right-content {
  padding-right: 0;
}
/************** Tree **************/
.right-panel {
  height: 100%;
  background: #3c3f41;
  border-left: solid 1px #323232;
  /* position: relative; */
  height:100%;
  width: 100%;
  margin-bottom:0;
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
  height: 30px;
  padding-top: 7px;
}
/deep/ .ant-tree {
  padding: 0 !important;
  width: 100%;
  /* border-left: solid 1px #323232; */
}
/deep/ .ant-tree-title {
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

</style>
