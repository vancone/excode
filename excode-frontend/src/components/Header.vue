<template>
  <div class="header">
    <img src="../assets/logo.png" class="logo">
    <h1>ExCode</h1>
    <div class="help-doc" style="float:right;height:55px;padding-right: 20px;cursor:pointer;">
      <i class="el-icon-question" style="display:inline-block;vertical-align:top;line-height:55px;"/>
      <span style="display:inline-block;line-height:55px;margin-left:6px;">Help</span>
    </div>
  </div>
</template>

<style scoped>
.header {
  background: black;
  color: white;
  height: 54px;
  text-align: left;
  padding-left: 10px;
}
h1 {
  margin-top: 0;
  margin-left: 10px;
  font-size: 22px;
  line-height: 55px;
  font-weight: 100;
  cursor: default;
  display: inline-block;
}
.logo {
  height: 35px;
  line-height: 55px;
  vertical-align: top;
  margin-top: 9px;
  display: inline-block;
}
</style>
