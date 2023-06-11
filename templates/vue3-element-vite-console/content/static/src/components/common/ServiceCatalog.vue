<template>
  <div class="service-drawer">
    <el-drawer modal-class="drawer-modal" :with-header="false" direction="ttb" v-model="props.visible">
      <div class="favorite-list">
        <h1>Favorites</h1>
      </div>

      <div style="width: calc(100% - 330px)">
        <el-input class="search-box" placeholder="Search with service names"></el-input>
        <div class="recently-visit-wrapper">
          <span class="label">Recently visited:</span>
          <span class="service-name">Passport</span>
          <span class="service-name">Ticket</span>
          <span class="service-name">Datable</span>
        </div>
        <div class="catalog-wrapper">
          <div class="service-list" v-for="catalog in businessCatalog">
            <h1>{{ catalog.name }}</h1>
            <a style="text-decoration: none;" v-for="menu in catalog.menus" :href="menu.link"><h2>{{ menu.name }}</h2></a>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import { queryBusinessCatalog } from "~/api";
import { IBusinessCatalog } from "~/api/types";

const props = defineProps<{
  visible: boolean,
}>();

const businessCatalog = reactive<Array<IBusinessCatalog>>([]);

function fetchBusinessDialog() {
  queryBusinessCatalog().then(({ data }) => {
    businessCatalog.splice(0, businessCatalog.length, ...data.data);
  })
}

fetchBusinessDialog();
</script>

<style lang="scss" scoped>
.service-drawer {
  color: black;
  display: flex;
  justify-content: left;

  :deep(.el-drawer) {
    height: 500px !important;
  }

  :deep(.el-drawer__body) {
    display: flex;
    justify-content: left;
    padding: 0;
  }

  .service-list {
    margin: 20px 0;
    width: 200px;

    h1 {
      border-bottom: solid 1px #ddd;
      font-size: 16px;
      font-weight: 500;
      margin: 0 0 5px 0;
      padding: 5px 15px;
    }

    h2 {
      color: #555;
      font-size: 14px;
      font-weight: 500;
      margin: 0;
      padding: 8px 15px;
    }

    h2:hover {
      background-color: #f3f6ed;
      border-radius: 2px;
      color: #8ba74f;
      cursor: pointer;
    }
  }
}

:deep(.drawer-modal) {
  position: absolute;
  top: 49px;
  height: calc(100% - 49px);
}

.favorite-list {
  width: 250px;
  border-right: solid 1px #ddd;

  h1 {
    font-size: 14px;
    font-weight: 500;
    margin: 20px;
  }
}

.catalog-wrapper {
  width: 100%;
  height: calc(100% - 121px);
  padding: 20px 40px;
  display: flex;
  gap: 50px;
  justify-content: left;
}

.search-box {
  margin: 20px 0 10px 40px;
  width: 500px;
}

.recently-visit-wrapper {
  padding-left: 40px;

  .label {
    color: #666;
    font-size: 14px;
    margin-right: 10px;
  }

  .service-name {
    font-size: 14px;
    padding: 0 15px;
  }

  .service-name:hover {
    color: #8ba74f;
    cursor: pointer;
    text-decoration: underline;
  }

  .service-name:nth-child(n + 3) {
    border-left: solid 1px #bbb;
  }
}
</style>
