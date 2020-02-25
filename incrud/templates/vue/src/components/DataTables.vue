<template>
  <div class="datatablesContainer">
    <el-button type="primary" class="addButton" @click="addTab(activeName)">New Item</el-button>

    <!-- <el-tabs editable closable v-model="activeName" type="border-card" class="tabs" @tab-click="switchTab">
      <el-tab-pane v-for="(table, index) in project.tables" :key="table.name" :label="table.name" :name="table.name + index"></el-tab-pane>
    </el-tabs> -->

    <el-table :data="items" style="margin-left:20px;" stripe>

## columns ##

      <el-table-column prop="operations" label="Operations" width="200">
        <template slot-scope="scope">
          <el-button  @click="editItem(scope.$index)">Edit</el-button>
          <el-popconfirm title="Are you sure to delete this item?" @onConfirm="deleteItem"
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
export default {
  name: 'Dependencies',
  props: [
    'datasource'
  ],
  data () {
    return {
      drawer: false,
      drawerTitle: 'Edit Dependency',
      activeName: '',
      edittingIndex: '',
      currentTable: {},
      edittedDependency: {
        groupId: "",
        artifactId: "",
        version: "",
        scope: ""
      },
      items: [
        {
          name: "11111",
          type: "22",
          version: '123'
        },
        {
          name: "1233333",
          type: "333"
        }
      ]
    }
  },
  methods: {
    switchTab(tab, event) {
      // alert(this.activeName)
      for (var i = 0; i < this.project.tables.length; i ++) {
        if (this.project.tables[i].name == this.activeName.substr(0, this.project.tables[i].name.length)) {
          this.currentTable = this.project.tables[i]
          // alert(JSON.stringify(this.currentTable))
          break
        }
      }
    },

    addTab(name) {
      this.project.tables.push({name:"New Table",columns:[]})
    },

    getList() {
      var that = this
      this.$axios({
        method:'get',
        url:'http://localhost:8080/## url ##',
      }).then((response) => {
        console.log(response)
        that.items = response.data.data
      }).catch((error) => {
        console.log(error)
      })
    },

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
  },
  mounted: function() {
    //this.activeName = this.project.tables[0].name + '0'
    // this.switchTab()
    this.getList()
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.datatablesContainer {
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
/* .topBar {
  background: #f8f9fb;
  border-bottom: solid 1px white;
  height: 110px;
  margin-bottom: 20px;
} */
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
.tabs {
  margin-left: 20px;
  width: 1000px;
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
