<template>
  <div>
    <el-card class="box-card" shadow="never">
      <div slot="header" class="clearfix">
        <span v-text="extensionId"></span>
        <el-switch style="float:right" v-model="value" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
      </div>
      <div class="text item">
        <p>
          <span>Allowed Headers</span> 127.0.0.1
        </p>
        <p>
          <span style="min-width:700px;padding-right:20px;">Allowed Methods</span>
          <el-checkbox-group v-model="checkList" style="display:inline-block">
            <el-checkbox label="*"></el-checkbox>
            <el-checkbox label="GET" v-if="!allSelected"></el-checkbox>
            <el-checkbox label="POST" v-if="!allSelected"></el-checkbox>
            <el-checkbox label="PUT" v-if="!allSelected"></el-checkbox>
            <el-checkbox label="DELETE" v-if="!allSelected"></el-checkbox>
          </el-checkbox-group>
        </p>
        <p>
          <span>Allowed Origins</span> 127.0.0.1
        </p>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'CrossOrigin',
  data () {
    return {
      extensionId: 'cross-origin',
      checkList: ['*'],
      allSelected: true,
      value: true
    }
  },
  methods: {
    loadExtensions () {}
  },
  mounted: function () {
    this.loadExtensions()
  },
  watch: {
    checkList: function (val, oldVal) {
      var allSelected = false
      for (var i in this.checkList) {
        if (this.checkList[i] === '*') {
          allSelected = true
          break
        }
      }
      this.allSelected = allSelected
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
.card-container {
  padding-left: 20px;
}
.box-card {
  margin-bottom: 20px;
  text-align: left;
}
</style>
