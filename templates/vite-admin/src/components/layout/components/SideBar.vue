<template>
    <div id="siderbar" class="scrollbar">
        <div class="logo" title="ExCode">
            <h1>__title__</h1>
        </div>
        <el-menu
            class="el-menu-vertical-demo"
            :collapse="isCollapse"
            :unique-opened="false"
            :collapse-transition="true"
            :router="true"
            :default-active="activeMenu"
        >
            <SideBarItem
                v-for="menu in menuList"
                :key="menu.path"
                :route="menu"
            />
        </el-menu>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { visibleChildrenFilter } from '@/utils/index'
import { routes } from '@/router/index'
import SideBarItem from './SideBarItem.vue'

export default defineComponent({
    name: 'SideBar',
    components: { SideBarItem },
    setup() {
        const isCollapse = ref(false)
        const route = useRoute()
        const activeMenu = computed(() => route.meta.activeMenu)
        const menuList = visibleChildrenFilter(routes)

        return {
            routes,
            isCollapse,
            activeMenu,
            menuList
        }
    }
})

</script>

<style lang="scss" scoped>
@import '@/styles/_variables.scss';

#siderbar {
    height: 100%;
    background-color: $color-bg-sidebar;
    text-align: left;
    overflow: hidden auto;
}

.el-menu {
    background-color: $color-bg-sidebar;
    border-right: none;
}

.logo {
    width: $width-sidebar;
    height: $height-header;
    cursor: default;

    h1 {
        color: white;
        margin-left: 20px;
        font-size: 18px;
        vertical-align: middle;
        line-height: $height-header;
    }
}
</style>