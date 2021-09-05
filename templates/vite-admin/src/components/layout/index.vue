<template>
    <div id="layout">
        <el-container class="layout-container">
            <el-aside width="auto">
                <SideBar />
            </el-aside>
            <el-container>
                <el-header>
                    <HeaderBar />
                </el-header>
                <el-main
                    v-loading="isLoading"
                    class="scrollbar"
                    element-loading-text="加载中..."
                    element-loading-background="rgba(0, 0, 0, 0.7)"
                >
                    <router-view v-slot="{ Component }">
                        <transition>
                            <component :is="Component" />
                        </transition>
                    </router-view>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import HeaderBar from './components/HeaderBar.vue'
import SideBar from './components/SideBar.vue'

export default defineComponent({
    name: 'Layout',
    components: {
        HeaderBar,
        SideBar
    },
    setup() {
        const isLoading = ref(false)

        return {
            isLoading
        }
    }
})
</script>

<style lang="scss" scoped>
@import '@/styles/_variables.scss';
#layout {
    height: 100%;
    background-color: #ecf0f5;
}

.el-aside {
    position: relative;
    height: 100vh;
    overflow: hidden;
}

.el-header {
    padding: 0;
}

.el-main {
    height: calc(100vh - #{$height-header})
}
</style>