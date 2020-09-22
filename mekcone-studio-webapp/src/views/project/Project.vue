<template>
  <div class="welcome-page">
    <!-- <GlobalHeader style="position:relative;z-index:2000;"/> -->
    <a-page-header
      style="border: 1px solid rgb(235, 237, 240);background:white;"
      :title="projectName"
      sub-title="1.0.0"
      @back="() => $router.go(-1)"
    >
    </a-page-header>
    <a-row style="height:calc(100% - 65px);">
      <a-col :span="5" style="text-align:left;height:100%;">
        <a-tree
          show-line
          style="background:white;height:inherit;padding:10px;"
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
      <a-col :span="4" style="text-align:left;height:100%;">
      </a-col>
    </a-row>
  </div>
</template>

<script>
import GlobalHeader from '@/components/GlobalHeader'
// import { getProjectList } from '@/api/project'

export default {
  name: 'Project',
  components: {
    GlobalHeader
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
      }]
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
      console.log('selected', selectedKeys, info)
      alert(JSON.stringify(selectedKeys))
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
.welcome-page {
  height:100%;
  margin-bottom:0;
}
</style>
