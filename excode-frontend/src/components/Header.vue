<template>
  <div class="header">
    <div class="title">
      <img src="../assets/logo.png" class="logo">
      <h1>ExCode</h1>
    </div>

    <div class="help-doc">
      <i class="el-icon-question"/>
      <span>Help</span>
    </div>
  </div>
</template>

<style scoped>
.header {
  background: #313b1c;
  color: white;
  height: 49px;
  text-align: left;
  display: flex;
  justify-content: space-between;
}
.title {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}
h1 {
  margin: 0;
  margin-left: 10px;
  font-size: 18px;
  font-weight: 100;
  cursor: default;
}
.logo {
  height: 30px;
  margin-left: 10px;
}
.help-doc {
  height: 49px;
  cursor: pointer;
  display: flex;
  align-items: center;
}
.help-doc span {
  line-height: 50px;
  margin-left: 6px;
  margin-right: 20px;
  font-size: 12px;
  color: #ddd;
}
</style>
