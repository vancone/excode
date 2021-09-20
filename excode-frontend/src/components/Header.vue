<template>
  <div class="header">
    <img src="../assets/logo.png" class="logo">
    <h1>ExCode</h1>
    <div class="help-doc">
      <i class="el-icon-question"/>
      <span>Help</span>
    </div>
  </div>
</template>

<style scoped>
.header {
  background: #222;
  color: white;
  height: 49px;
  text-align: left;
  padding-left: 10px;
}
h1 {
  margin-top: 0;
  margin-left: 10px;
  font-size: 18px;
  line-height: 50px;
  font-weight: 100;
  cursor: default;
  display: inline-block;
}
.logo {
  height: 30px;
  line-height: 55px;
  vertical-align: top;
  margin-top: 10px;
  display: inline-block;
}
.help-doc {
  float: right;
  height: 50px;
  padding-right: 20px;
  cursor: pointer;
}
.help-doc span {
  display: inline-block;
  line-height: 50px;
  margin-left: 6px;
  font-size: 12px;
  color: #ddd;
}
</style>
