<template>
  <div class="dependenciesContainer">
    <el-button type="primary" class="addButton" @click="newItem">New Dependency</el-button>
    <el-table :data="project.dependencies" style="width:1000px;margin-left:20px;" stripe>
      <el-table-column prop="groupId" label="Group ID" width="250"></el-table-column>
      <el-table-column prop="artifactId" label="Artifact ID" width="250"></el-table-column>
      <el-table-column prop="version" label="Version" width="150"></el-table-column>
      <el-table-column prop="scope" label="Scope" width="150"></el-table-column>
      <el-table-column prop="operations" label="Operations" width="200">
        <template slot-scope="scope">
          <el-button  @click="editItem(scope.$index)">Edit</el-button>
          <el-popconfirm title="Are you sure to delete this dependency?" @onConfirm="deleteItem"
            confirmButtonText="Confirm"
            cancelButtonText='Cancel'>
            <el-button type="danger" slot="reference">Delete</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

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
