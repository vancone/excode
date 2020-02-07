<template>
  <div class="dependenciesContainer">
    <el-form ref="form" :model="form" label-width="120px" class="form">
      <h1>Admin Frontend</h1>
      <el-button type="primary" disabled>Generate Vue.js Project (Premium only)</el-button>
      <el-button type="primary">Generate Destination Project</el-button>
      <h1>Backend</h1>
      <el-button type="primary">Generate Project File</el-button>
      <el-button type="primary" disabled>Generate Maven Project (Premium only)</el-button>
      <el-button type="primary">Generate JAR Package</el-button>
    </el-form>

    <el-drawer
      :title="drawerTitle"
      :visible.sync="drawer"
      direction="rtl"
      :before-close="close"
      append-to-body="false"
      >
        <el-input placeholder="groupId" v-model="edittedDependency.groupId" class="drawerInput"/>
        <el-input placeholder="artifactId" v-model="edittedDependency.artifactId" class="drawerInput"/>
        <el-input placeholder="version (Optional)" v-model="edittedDependency.version" class="drawerInput"/>
        <el-input placeholder="scope (Optional)" v-model="edittedDependency.scope" class="drawerInput"/>
        <el-button type="primary" class="drawerButton" @click="save">OK</el-button>
    </el-drawer>
  </div>
</template>

<script>
import SelectList from "@/components/SelectList"
export default {
  name: 'Dependencies',
  components: {SelectList},
  props: [
    'project'
  ],
  data () {
    return {
      drawer: false,
      drawerTitle: 'Edit Dependency',
      edittingIndex: '',
      mysqlUrl: '',
      mysqlPassword: '',
      edittedDependency: {
        groupId: "",
        artifactId: "",
        version: "",
        scope: ""
      }
    }
  },
  methods: {
    editItem(index) {
      this.edittingIndex = index
      this.drawerTitle = 'Edit Dependency'
      this.edittedDependency = this.project.dependencies[index]
      this.drawer = true
      //alert(this.project.dependencies[index].artifactId)
    },
    deleteItem(index) {
      this.project.dependencies.splice(index, 1)
      //this.project.dependencies.delete(index)
    },
    close(done) {
      done();
      /* this.$confirm('Are you sure you want to close this?')
          .then(_ => {
            done();
          })
          .catch(_ => {}); */
    },
    save() {
      //this.project.dependencies[this.edittingIndex] = this.edittedDependency
      if (this.drawerTitle == 'New Dependency') {
        this.project.dependencies.push(
          this.edittedDependency
        )
      }
      this.drawer = false
    },
    newItem() {
      this.drawerTitle = 'New Dependency'
      this.drawer = true
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.dependenciesContainer {
  background: #edf1f2;
  /* color: #bbbbbb; */
  /* padding: 5px; */
  text-align: left;
  height: 100%;
  width: calc(100% - 200px);
  position: fixed;
  left: 201px;
  top: 0px;
  z-index: 0;
}
.form {
  width: calc(100% - 100px);
  margin-left: 20px;
  padding-top: 20px;
}
.form h1 {
  font-size: 22px;
  /* margin-left: 20px; */
  font-weight: normal;
}
.topBar {
  background: #f8f9fb;
  border-bottom: solid 1px white;
  height: 110px;
  margin-bottom: 20px;
}
.addButton {
  margin: 10px;
  margin-left: 20px;
}
.buttonBar {
  background: none;
  padding-top: 15px;
  padding-left: 20px;
  height: 35px;

}
.buttonBar img {
  cursor: pointer;
}
.topBar h1 {
  font-weight: bold;
  font-size: 28px;
  margin: 0;
  margin-top: 12px;
  margin-left: 20px;
  color: black;
}
.button {
  border-radius: 5px;
  border: none;
  outline: none;
  background: #2091f6;
  color: white;
  padding: 15px;
  margin-left: 30px;
  cursor: pointer;
  font-size: 16px;
}
.drawerInput {
  margin-left: 20px;
  margin-bottom: 15px;
  display: block;
  width: calc(100% - 40px);
}
.drawerButton {
  margin-left: 20px;
  width: calc(100% - 40px);
}
</style>
