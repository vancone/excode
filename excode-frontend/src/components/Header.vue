<template>
  <div class="header">
    <div class="title">
      <img src="../assets/logo.png" class="logo">
      <h1>VanCone</h1>
      <h2>ExCode</h2>
    </div>

    <div class="help-doc">
      <i class="el-icon-question"/>
      <span>Help</span>
    </div>
  </div>
</template>

<style scoped>
.header {
  background: #333;
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
  margin: 0 15px 0 10px;
  padding-right: 15px;
  font-size: 18px;
  font-weight: 500;
  cursor: default;
  border-right: solid 1px #666;
}
h2 {
  margin: 0;
  padding-right: 15px;
  font-size: 14px;
  font-weight: 500;
  color: #999;
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
