<template>
  <div class="global-header">
    <a-page-header
      class="page-header"
      :title="title"
      sub-title="subTitle"
      @back="backFun"
    >
    </a-page-header>
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
.global-header {
  height: 70px;
  width: 100%;
  /* background-color: #3c3f41; */
  /* box-shadow: 0px 3px 3px rgba(0, 0, 0, 0.1); */
  z-index: 2133333;
  text-align: left;
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
/deep/ .ant-page-header-heading-title {
  color: #ddd;
}
/deep/ .ant-page-header-heading-sub-title {
  color: #ddd;
}
/deep/ .anticon-arrow-left {
  color: #bbb;
}
</style>
