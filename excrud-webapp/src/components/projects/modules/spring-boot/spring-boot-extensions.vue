<template>
  <div class="hello">
    <el-row :gutter="20">
      <el-col :span="19">
        <h2 class="projects-title">Extensions of Spring Boot Module</h2>
        <div class="card-container">
          <cross-origin v-if="crossOriginVisible"></cross-origin>
          <lombok v-if="lombokVisible"></lombok>
        </div>
      </el-col>
      <el-col :span="5">
        <div style="text-align: left;padding-top: 40px;">
          <input ref="file" type="file" accept=".xml,.json" style="display:none" @change="importProject($event)">
          <el-button type="primary" style="height: 35px;width: 150px;padding-top:10px;margin-bottom:10px">Add extension</el-button><br>
          <!-- <el-button @click="$refs.file.click()" style="height: 35px;width: 150px;padding-top:10px">Import</el-button> -->
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import CrossOrigin from '@/components/projects/modules/spring-boot/extensions/cross-origin'
import Lombok from '@/components/projects/modules/spring-boot/extensions/lombok'
export default {
  name: 'InitialPage',
  components: {
    CrossOrigin, Lombok
  },
  data () {
    return {
      crossOriginVisible: false,
      lombokVisible: false
    }
  },
  methods: {
    loadExtensions () {
      for (var item in this.$utils.extensions) {
        console.log(this.$utils.extensions[item].id)
        var id = this.$utils.extensions[item].id
        if (id == 'cross-origin') {
          this.crossOriginVisible = true
        }
        if (id == 'lombok') {
          this.lombokVisible = true
        }
      }
    }

  },
  mounted: function () {
    this.loadExtensions()
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
  margin-left: 30px;
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
.card-container {
  padding-left: 30px;
  padding-right: 30px;
  overflow-y: auto;
}
.box-card {
  margin-bottom: 20px;
  text-align: left;
}
</style>
