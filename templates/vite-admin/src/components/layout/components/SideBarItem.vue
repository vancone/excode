<template>
    <el-submenu
        v-if="route.children && route.children.length > 0"
        :index="route.name"
    >
        <template #title>
            <i class="icon-nav" :class="rooute.meta.icon" />
            <span>{{ route.meta.title }}</span>
        </template>
        <SideBarItem
            v-for="sub in visibleChildrenFilter(route.children)"
            :key="sub.path"
            :route="sub"
        />
    </el-submenu>
    <el-menu-item
        v-else
        :index="route.meta.activeMenu"
    >
        <i class="icon-nav" :class="route.meta.icon || ''" />
        <template #title>
            <span>{{ route.meta.title }}</span>
        </template>
    </el-menu-item>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { visibleChildrenFilter } from '@/utils/index'

export default defineComponent({
    name: 'SideBarItem',
    props: {
        route: {
            type: Object,
            default: () => ({})
        }
    },
    setup() {
        return {
            visibleChildrenFilter
        }
    }
})
</script>

<style lang="scss" scoped>
@import '@/styles/_variables.scss';

.el-menu {

    .el-menu-item {
        &.is-active {
            background-color: $color-bg-menu-active !important;
        }

        &:hover {
            color: #fff !important;
            background-color: $color-bg-menu-active !important;

            >i {
                color: inherit;
            }
        }
    }
}

.el-menu-vertical-demo {
    &:not(.el-menu--collapse) {
        width: 200px;
    }
}
</style>

<style lang="scss">
.el-menu--collapse {
    >.sidebar-item {
        .el-submenu__title {
            >span {
                height: 0;
                width: 0;
                overflow: hidden;
                visibility: hidden;
                display: inline-block;
            }

            >.el-submenu__icon-arrow {
                display: none;
            }
        }
    }
}
</style>