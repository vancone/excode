<template>
  <div class="header">
    <div class="title">
      <img src="../../assets/logo.png" class="logo" />
      <h1>VanCone</h1>
      <div class="title-tab">
        <img src="../../assets/console.svg"/>
        <span>Console</span>
      </div>
      <div class="title-tab" @click="serviceDrawerVisible = !serviceDrawerVisible">
        <img src="../../assets/catalog2.svg"/>
        <span>Catalog</span>
      </div>
    </div>
    <ServiceCatalog :visible="serviceDrawerVisible" />

    <div class="right">
      <!-- <div class="help-doc">
        <i class="el-icon-question" />
        <span>Help</span>
      </div> -->
      <el-dropdown class="lang-dropdown" @command="handleSwitchLanguage">
        <span class="el-dropdown-link">
          {{ language.name }}
          <el-icon class="el-icon--right">
            <arrow-down />
          </el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item v-for="(lang, index) in languageList" :key="lang.id" :command="index">
              {{ lang.name }}
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-dropdown>
        <div class="user-wrapper">
          <el-avatar :size="28" src="" />
          <span class="">{{ username }}</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <!-- <el-dropdown-item>Personal Center</el-dropdown-item>
            <el-dropdown-item divided @click="handleLogout">Sign out</el-dropdown-item> -->
            <el-dropdown-item @click="handleLogout">{{ $t('common.header.signOut') }}</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import ServiceCatalog from "./ServiceCatalog.vue";
import { ArrowDown } from "@element-plus/icons-vue";
import { onMounted, reactive, ref } from "vue";
import { logout, queryUser } from "~/api";
import i18n from "~/i18n/index";

const serviceDrawerVisible = ref(false);
const username = ref('');
const language = reactive<any>({
  id: 'en',
  name: 'English'
});
const languageList = reactive<any>([
  {
    id: 'en',
    name: 'English'
  },
  {
    id: 'zh',
    name: '简体中文'
  }
])

onMounted(() => {
  const lang = sessionStorage.getItem('lang');
  if (lang !== null) {
    Object.assign(language, languageList[lang]);
    i18n.global.locale = language.id;
  }
  
  username.value = getCookie('passport_username');
  if (username.value === '') {
    queryUser().then(({ data }) => {
      if (data.code == 0) {
        username.value = data.data.name ?? data.data.displayId;
      }
    });
  }
});

function getCookie(cname: string) {
  var name = cname + "=";
  var ca = document.cookie.split(';');
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') c = c.substring(1);
    if (c.indexOf(name) != -1) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

function handleLogout() {
  logout().then(() => {
    const currentUrl = escape(window.location.href);
    window.location.href = `//${import.meta.env.VITE_PASSPORT_BASE_URL}/?from=${currentUrl}#login`;
  });
}

function handleSwitchLanguage(command: string | number | object) {
  Object.assign(language, languageList[command as number]);
  i18n.global.locale = language.id;
  sessionStorage.setItem('lang', command as string)
}
</script>

<style lang="scss" scoped>
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

  span {
    color: #ddd;
    cursor: pointer;
    font-size: 14px;
  }

  span:hover {
    color: white;
  }
}

.logo {
  height: 30px;
  margin-left: 10px;
}

.right {
  height: 49px;
  cursor: pointer;
  display: flex;
  align-items: center;

  .lang-dropdown {
    color: #ddd;
    margin-right: 10px;
    font-size: 12px;
  }

  .user-wrapper {
    display: flex;
    align-items: center;
    justify-content: left;
    height: 49px;
    width: 100px;
    padding: 0 10px;

    span {
      margin-left: 0px;
      margin-right: 10px;
      font-size: 12px;
      color: #ddd;
    }
  }

  .user-wrapper:hover {
    background: #222;
  }
}

.help-doc span {
  line-height: 50px;
  margin-left: 6px;
  margin-right: 20px;
  font-size: 12px;
  color: #ddd;
}

.title-tab {
  display: flex;
  align-items: center;
  gap: 8px;
  padding-left: 8px;
  padding-right: 8px;
  cursor: pointer;

  img {
    height: 14px;
  }

  span {
    font-size: 12px;
  }
}
</style>
