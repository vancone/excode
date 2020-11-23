<template>
  <div class="tool-bar">
    <button class="btn button-download"></button>
    <button class="btn button-execute"></button>
    <!-- <img src="../assets/download.svg" class="button-download" /> -->
  </div>
</template>

<script>
export default {
  name: 'GlobalHeader',
  props: [
    'title', 'subTitle', 'logo', 'backFlag', 'backgroundColor'
  ],
  data () {
    return {
    }
  },
  watch: {
    backFun: () => {
      alert(this.title)
      if (this.backFlag === 'true') {
        alert('yes')
        return () => null
      } else {
        return undefined
      }
      // return this.backFlag == true? (() => null): undefined
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.tool-bar {
  height: 30px;
  width: 100%;
  background-color: #3c3f41;
  border-bottom: solid 1px #323232;
  /* box-shadow: 0px 3px 3px rgba(0, 0, 0, 0.1); */
  z-index: 2133333;
  text-align: right;
  padding-left: 10px;
}
.global-header img {
  height: 45px;
  display: inline;
  line-height: 70px;
  margin-left: 10px;
}
.global-header h1 {
  font-size: 18px;
  margin: 0;
  margin-left: 5px;
  vertical-align: middle;
  height: 70px;
  line-height: 70px;
  color: #ddd;
  font-weight: normal;
  display: inline;
}
.button-download {
  height: 25px;
  width: 25px;
  line-height: 30px;
  background-color: transparent;
  border: none;
  background-image: url('../assets/download.svg');
  background-repeat: no-repeat;
  background-size: 20px 20px;
  background-position: 2px 2px;
  cursor: pointer;
}
.button-execute {
  height: 25px;
  width: 25px;
  line-height: 30px;
  background-color: transparent;
  border: none;
  background-image: url('../assets/play.svg');
  background-repeat: no-repeat;
  background-size: 20px 20px;
  background-position: 2px 2px;
  cursor: pointer;
}
.btn:hover {
  background-color: #4c5052;
  border-radius: 3px;
}
</style>
