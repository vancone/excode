<template>
  <div class="topBarContainer">
    <el-page-header class="header" @back="goBack" :content="title"></el-page-header>
  </div>
</template>

<script>
export default {
  name: 'TopBar',
  props: [
    "title"
  ],
  data () {
    return {

    }
  },
  methods: {
    goBack() {
      // alert(this.$route.from)
      this.$router.go(-1)
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.topBarContainer {
  text-align: left;
  background: white;
  border-bottom: solid 1px #ddd;
  height: 50px;
  padding-left: 20px;
  cursor: pointer;
  position: fixed;
  left: 200px;
  top:0;
  width: 100%;
}
.header {
  line-height: 50px;
  vertical-align: middle;
}
</style>
