<template>
  <div class="hello">
    <el-row :gutter="20">
      <el-col :span="4">
        <div>&nbsp;</div>
      </el-col>
      <el-col :span="16">
        <h2 class="projects-title">Projects</h2>
        <el-table :data="tableData" class="project-table">
          <el-table-column prop="date" label="Project name" ></el-table-column>
          <el-table-column prop="name" label="Creator" width="200"></el-table-column>
          <el-table-column prop="address" label="Modified time" width="200"></el-table-column>
        </el-table>
      </el-col>
      <el-col :span="4">
        <div style="text-align: left;padding-top: 40px;">
          <el-button type="primary" style="height: 35px;width: 150px;padding-top:10px;margin-bottom:10px">New project</el-button><br>
          <el-button style="height: 35px;width: 150px;padding-top:10px">Import</el-button>
          <!-- <button class="button-create" @click="edit">New project</button> -->
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'InitialPage',
  data () {
    return {
      tableData: [
        {
          date: 'MekCone Mall',
          name: 'Tenton Lien',
          address: '13:55, Jun. 22, 2020'
        },
        {
          date: 'MekCone Blog',
          name: '王小虎',
          address: '13:55, Jun. 22, 2020'
        },
        {
          date: '2016-05-01',
          name: '王小虎',
          address: '13:55, Jun. 22, 2020'
        },
        {
          date: '2016-05-03',
          name: '王小虎',
          address: '13:55, Jun. 22, 2020'
        }
      ]
    }
  },
  methods: {
    edit() {
      this.$router.push('/edit')
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1,
h2 {
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
.projects-title {
  text-align: left;
  font-size: 18px;
  margin-top: 40px;
  margin-left: 20px;
  margin-bottom: 20px;

}
.project-table {
  margin-left: 20px;
  width: calc(100% - 40px);
  background: none;
}
.button-create {
  background-color: #195f71;
  color: white;
  border-radius: 2px;
  height: 30px;
  width: 150px;
  outline: none;
  border: none;
  margin-top: 40px;
  cursor: pointer;
}

.el-table--fit{
        /* padding: 20px; */
}
.el-table, .el-table__expanded-cell {
    background-color: transparent;
}

.el-table tr {
    background-color: transparent!important;
}
.el-table--enable-row-transition .el-table__body td, .el-table .cell{
    background-color: transparent;
}
</style>
