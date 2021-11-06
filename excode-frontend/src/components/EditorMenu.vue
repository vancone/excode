<template>
  <el-menu
    default-active="2"
    class="menu"
    @open="handleOpen"
    @close="handleClose"
  >
    <el-menu-item index="1"><img class="icon" src="../assets/database.svg">Data Table</el-menu-item>
    <el-menu-item index="2"><img class="icon" src="../assets/spring.svg">Spring Boot</el-menu-item>
    <el-menu-item index="3"><img class="icon" src="../assets/vuejs.svg">Vite Admin</el-menu-item>
  </el-menu>
</template>

<script></script>

<style scoped>
.menu {
  width: 200px;
}
.icon {
    height: 18px;
    margin-right: 10px;
}
</style>
