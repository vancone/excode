<template>
  <div class="global-header">
    <h1>MekCone Studio</h1>
  </div>
</template>

<script>
export default {
  name: 'GlobalHeader',
  data () {
    return {
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.global-header {
  height: 50px;
  width: 100%;
  background-color: white;
  box-shadow: 0px 3px 3px rgba(0, 0, 0, 0.1);
  z-index: 2133333;
}
.global-header h1 {
  font-size: 18px;
  margin: 0;
  vertical-align: middle;
  height: 50px;
  line-height: 50px;
  font-weight: normal;
}
</style>
