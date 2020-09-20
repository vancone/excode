<template>
  <div class="welcome-page">
    <GlobalHeader/>
    <a-row>
      <a-col :span="12" style="padding: 20px;">
        <a-table :columns="columns" :data-source="data" style="background:#fff;border-radius:5px;overflow:hidden;" :body-style="{padding:10}" :row-key="data => data.id">
          <a slot="name" slot-scope="text,record" @click="loadProject(record)">{{text}}</a>
        </a-table>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import GlobalHeader from '@/components/GlobalHeader'
import { getProjectList } from '@/api/project'
export default {
  name: 'Welcome',
  components: {
    GlobalHeader
  },
  data () {
    return {
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
  color: #42b983;
}
</style>
