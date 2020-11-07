<template>
  <div class="project-page">
    <!-- <GlobalHeader style="position:relative;z-index:2000;"/> -->
    <a-page-header
      class="page-header"
      :title="projectName"
      @back="() => $router.go(-1)"
    >
    </a-page-header>
    <a-row style="height:calc(100% - 65px);">
      <a-col :span="5" style="text-align:left;height:100%;">
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
      </a-col>

      <a-col :span="19" style="text-align:left;height:100%;">
        <!-- <div :style="{ marginBottom: '16px' }">
          <a-button @click="add">
            ADD
          </a-button>
        </div> -->
        <a-tabs v-model="activeKey" hide-add type="editable-card" @edit="onEdit" style="height:100%">
          <a-tab-pane key="projectProperties" tab="Project" closable="closable">
            <project-properties />
          </a-tab-pane>

          <!-- <a-tab-pane v-for="pane in panes" :key="pane.key" :tab="pane.title" :closable="pane.closable">
            {{ pane.content }}
          </a-tab-pane> -->
        </a-tabs>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import GlobalHeader from '@/components/GlobalHeader'
import ProjectProperties from '@/components/ProjectProperties'
// import { getProjectList } from '@/api/project'

export default {
  name: 'Project',
  components: {
    GlobalHeader, ProjectProperties
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
/deep/ .ant-page-header-heading-title {
  color: #bbb;
}
/deep/ .ant-page-header-heading-sub-title {
  color: #bbb;
}
/deep/ .anticon-arrow-left {
  color: #bbb;
}
/deep/ .ant-page-header {
  /* height: 60px; */
}
/deep/ .ant-tabs-tab {
  background: #1f2022;
}
/deep/ .ant-tabs-bar {
  border: solid 1px #515151;
  background: #3c3f41;
}
/deep/ .ant-tabs.ant-tabs-card {
  background: #2b2b2b;
}
/deep/ .ant-tabs-tab {
  background-color: #4e5254;
}
/deep/ .ant-tabs-tab-active {
  background-color: #4e5254;
}
/deep/ .ant-tree-title {
  color: #bbb;
}
</style>
