<template>
  <div class="topBarContainer">
    <div class="buttonContainer">
      <button>Database Tables</button>
      <button>RESTful APIs</button>
    </div>

    <!-- <div class="bottomBar">
      <button id="ButtonViewSource" @click="viewSource"></button>
    </div> -->
  </div>
</template>

<script>
export default {
  name: 'Workplace',
  data () {
    return {
      appName: 'Webber',
      sourceJSON: {
        projectName: "unnamed project",
        database: {
          host: "localhost:3306",
          password: "123456",
          tables: [
          {
            defaultTables: [
              {
                columnName: "defaultColumn",
                generateAPI: true
              }
            ]
          }
          ]
        }  
      }
    }
  },
  methods: {
   /*  viewSource() {
      alert(JSON.stringify(this.sourceJSON))
    } */
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.topBarContainer {
  background: #f7f9fa;
  color: #bbbbbb;
  padding: 5px;
  text-align: left;
  height: calc(100% - 41px);
  width: 100%;
  position: fixed;
  left: 240px;
  top: 50px;
}
h1 {
  font-weight: normal;
  font-size: 18px;
  margin: 0;
}
.buttonContainer {
  position: absolute;
  margin: auto;
  left: 0;
  right: 0;
  top: 100px;
  bottom: 0;
  width: 500px;
  height: 300px;
}
.buttonContainer button {
  width: 200px;
  height: 200px;
  border-radius: 5px;
  background: #181818;
  color: #bbb;
  outline: none;
  border: none;
  margin-left: 10px;
  margin-right: 10px;
  cursor: pointer;
}
.buttonContainer button:hover {
  background: #2a2d2e;
}
.bottomBar {
  background: #007acc;
  height: 30px;
  width: 100%;
  position: fixed;
  bottom: 0;
  left: 0;
}
#ButtonViewSource {
  height: 30px;
  width: 50px;
  background: #16825d;
  border: none;
  outline: none;
  cursor: pointer;
}

#ButtonViewSource:hover {
  background:#329171;
}
</style>
