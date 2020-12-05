<template>
  <div id="nav">
    <left-bar></left-bar>
    <!-- <router-link to="/">Home</router-link> |
    <router-link to="/about">About</router-link>
    <el-button>默认按钮</el-button>
    <button>Check JAR</button> -->
  </div>
  <router-view style="float:right;width:calc(100% - 220px)"/>
</template>

<script>
import LeftBar from '@/components/LeftBar'
export default {
  components: {
    LeftBar
  }
}
</script>

<style lang="stylus">
#app
  font-family Avenir, Helvetica, Arial, sans-serif
  -webkit-font-smoothing antialiased
  -moz-osx-font-smoothing grayscale
  text-align center
  color #2c3e50
  height 100%
  width 100%

#nav
  display inline-block
  height 100%
  width 200px
  float left
</style>
