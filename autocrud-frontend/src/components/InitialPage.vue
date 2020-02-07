<template>
  <div class="initialPageContainer">
    <h1>{{appName}}</h1>
    <div class="buttonContainer">
      <!-- <router-link v-for= "button in buttons" :to="button.to" :key="button.text">
        <button v-text="button.text" :style="{background: button.color,color: button.fontColor}"></button>
      </router-link> -->
      <button></button>
      <button>Create a project</button>
      <button @click="open">Open from local machine</button>
      <input type="file" name="file" id="file" class="file" @change="readFile"/>
      <!-- <button>Login</button>
      <router-link to="/project"><button>Create a project</button></router-link>
      <button>Open from local machine</button> -->
    </div>

  </div>
</template>

<script>
export default {
  name: 'InitialPage',
  data () {
    return {
      appName: 'AutoCRUD',
      buttons: [
        {text: "Sign in", color: "white", fontColor: "black", to: ""},
        {text: "Create a project", color: "#2091f6", to: "/project"},
        {text: "Open from local machine", color: "#2091f6", to: ""}
      ]
    }
  },
  methods: {
    open() {
      document.getElementById('file').click()
    },
    readFile() {
      var f = document.getElementById('file').files[0]
      if (f == null) {
        alert('Invalid project file')
        return
      }
      var reader = new FileReader()
      reader.readAsText(f)
      var that = this
      reader.onload = function(e) {
        that.$emit('submitProject', JSON.parse(e.target.result))
        that.$router.push('/overview')
      }
    }
  },
  mounted: function() {
    
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.initialPageContainer {
  position: fixed;
  left: 0;
  top: 0;
  background: #f7f9fa;
  color: black;
  padding: 5px;
  text-align: center;
  height: 100%;
  width: 100%;
  z-index: 1000;
}
h1 {
  font-weight: bold;
  font-size: 72px;
  margin: 0;
  margin-top: 120px;
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
  width: 400px;
  height: 80px;
  border-radius: 5px;
  background: #2091f6;
  color: white;
  outline: none;
  border: none;
  margin-top: 10px;
  margin-bottom: 10px;
  cursor: pointer;
  font-size: 22px;
}
.buttonContainer button:hover {
  background: #0e416f;
}
.file {
  opacity: 0;
}
</style>
