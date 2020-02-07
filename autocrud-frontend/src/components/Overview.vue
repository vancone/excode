<template>
  <div class="overviewContainer">
    <p class="field">
      Group ID&nbsp;&nbsp;&nbsp;
      <input v-model="project.groupId"/>
    </p>
    <p class="field">
      Artifact ID&nbsp;&nbsp;
      <input v-model="project.artifactId"/>
    </p>

    <p class="field">
      Version&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input v-model="project.version"/>
    </p>

    <p class="field">
      Description
      <input v-model="project.description"/>
    </p>
    <button class="button" @click="generateJSON">Generate JSON</button>
    <button class="button" @click="generateJSON">Generate JAR Package</button>
  </div>
</template>

<script>
export default {
  name: 'Overview',
  components: {},
  props: [
    'project'
  ],
  data () {
    return {
      appName: 'AutoCRUD'
    }
  },
  methods: {
    generateJSON() {
      alert(JSON.stringify(this.project))
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.overviewContainer {
  /* background: white; */
  /* color: #bbbbbb; */
  /* padding: 5px; */
  text-align: left;
  height: 100%;
  width: calc(100% - 200px);
  position: fixed;
  left: 201px;
  top: 0px;
}
.topBar {
  background: #f8f9fb;
  border-bottom: solid 1px white;
  height: 110px;
  
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
.field {
  margin-left: 30px;
}
.field input {
  height: 35px;
  border-radius: 5px;
  outline: none;
  border: solid 1px #ddd;
  padding-left: 10px;
  margin-left:5px;
  width: 300px;
  font-size: 16px;
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

</style>
