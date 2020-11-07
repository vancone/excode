<template>
  <div class="welcome-page">
    <div class="welcome-header">
      <h1>MekCone Studio</h1>
    </div>
    <!-- <GlobalHeader title="MekCone Studio" subTitle="0.1.0" backFlag="true"/> -->
    <a-row>
      <a-col :xs="{span: 1}" :lg="{span: 6}"></a-col>
      <a-col :xs="{span: 22}" :lg="{span: 12}" style="padding: 20px;text-align:left">
        <a-row style="margin-top:60px;">
          <a-col :span="12">
            <h1 style="color:#bbb;">Projects</h1>
          </a-col>
          <a-col :span="12" style="text-align:right">
            <a-button class="panel-button" @click="showModal">Create</a-button>
          </a-col>
        </a-row>
        <a-table :columns="columns" :data-source="data" style="border-radius:5px;overflow:hidden;" :body-style="{padding:10}" :row-key="data => data.id">
          <a slot="name" slot-scope="text,record" @click="loadProject(record)">{{text}}</a>
        </a-table>
      </a-col>
    </a-row>
    <project-properties ref="projectCreator"></project-properties>
  </div>
</template>

<script>
import GlobalHeader from '@/components/GlobalHeader'
import { getProjectList } from '@/api/project'
import ProjectProperties from '@/components/ProjectProperties'
export default {
  name: 'Welcome',
  components: {
    GlobalHeader, ProjectProperties
  },
  data () {
    return {
      logo: 'https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1603818378&di=4a493212914cc3c6e2b43a19b5841c1f&src=http://i2.hdslb.com/bfs/archive/1c7af041d579ea9318ca0312aa4f59e9cc056687.jpg',
      columns: [
        {
          title: 'Name',
          dataIndex: 'name',
          key: 'name',
          scopedSlots: { customRender: 'name' }
        },
        {
          title: 'Modified Time',
          dataIndex: 'modifiedTime',
          key: 'modifiedTime',
          width: 200
        }
      ],
      data: []
    }
  },
  created () {
    getProjectList().then((res) => {
      this.data = res.data.content
    })
  },
  methods: {
    loadProject (project) {
      this.$store.commit('id', project.id)
      this.$store.commit('name', project.name)
      this.$store.commit('modules', project.modules)
      this.$router.push('/project')
    },
    showModal () {
      this.$refs.projectCreator.showModal()
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
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
  color: #bbb;
}
.welcome-header {
  height: 70px;
  width: 100%;
  /* background-color: #3c3f41; */
  /* box-shadow: 0px 3px 3px rgba(0, 0, 0, 0.1); */
  z-index: 2133333;
  text-align: left;
}
.welcome-header img {
  height: 45px;
  display: inline;
  line-height: 70px;
  margin-left: 10px;
}
.welcome-header h1 {
  font-size: 18px;
  margin: 0;
  margin-left: 20px;
  vertical-align: middle;
  height: 70px;
  line-height: 70px;
  color: #bbb;
  font-weight: normal;
  display: inline;
}
/deep/ .ant-table-wrapper {
  background:#323232;
  border: solid 1px #373737;
}
/deep/ .ant-table-thead tr th {
  background: #3b3b3b;
  color: #bbb;
  border-bottom: solid 1px #333;
}
/deep/ .ant-table-row td {
  border-bottom: solid 1px #444;
  color: #bbb;
}
/* /deep/ .ant-table-row:hover {
  background: #666;
} */
/deep/ .ant-table .ant-table-tbody > tr:hover:not(.ant-table-expanded-row) > td {
  background: #214283;
  cursor: pointer;
}
/deep/ .ant-pagination-item {
  background: #4b6eaf;
  border: solid 1px #365880;
}
/deep/ .ant-pagination-item a {
  color: #bbb;
}
/deep/ .ant-pagination-item-link {
  background: #2b2b2b;
  border: solid 1px #666;
  color: #bbb;
}
/deep/ .ant-pagination {
  padding-right: 10px;
}
.project-manager {
  height: 100px;
  margin-bottom: 20px;
  /* width: calc(100% - 20px); */
  border-radius: 5px;
  background: #3c3f41;
  border: none;
}
/deep/ .ant-card-body {
  padding: 10px;
}
.panel-button {
  background: #313335;
  border-radius: 5px;
  height: 30px;
  /* width: 50px; */
  background: #4b6eaf;
  border: solid 1px #365880;
  color: #bbb;
  text-align: center;
  /* line-height: 80px; */
  margin-top: 10px;
  cursor: pointer;
}
</style>
