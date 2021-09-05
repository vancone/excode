<template>
    <div id="head">
        <ul class="module-list">
            <li class="module-item">项目规划</li>
            <li class="module-item">代码生成</li>
            <li class="module-item active">代码执行</li>
            <li class="module-item">项目部署</li>
        </ul>
        <div class="user-wrap">
            <div class="language">
                <el-dropdown trigger="click" size="medium">
                    <span class="el-dropdown-link">
                        {{ language }}
                        <i class="el-icon-arrow-down el-icon--right" />
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item
                                v-for="(lang, key) in languageMap"
                                :key="key"
                                :command="key"
                                :disabled="language === lang"
                            >
                                {{ lang }}
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
            <div class="user">
                <el-dropdown trigger="click" size="medium" @command="logoutCommand">
                    <span class="el-dropdown-link">
                        <i class="el-icon-user-solid el-icon--left" />
                        {{ userName }}
                        <i class="el-icon-arrow-down el-icon--right" />
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item>退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
// import { getUser } from '@/api/index'
// import { logout } from '@/utils/index'

export default defineComponent({
    name: 'HeaderBar',
    setup() {
        const languageMap = {
            zh: '中文',
            en: 'English'
        }
        const userName = ref('')
        const language = '中文'

        const logoutCommand = () => {
            // logout()
        }

        const fetchData = async () => {
            // getUser().then(({ data }) => {
            //     userName.value = data.data.givenName
            // })
        }
        fetchData()

        return {
            language,
            userName,
            languageMap,
            logoutCommand
        }
    }
})
</script>


<style lang="scss" scoped>
@import '@/styles/_variables.scss';

#head {
    position: relative;
    z-index: 10;
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: $height-header;
    padding: 0 20px;
    background-color: white;
    box-shadow: 0px 1px 3px 0px rgba(0, 0, 0, .2);
}

.user-wrap {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    height: 100%;
}

.language {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-right: 20px;

    &:after {
        content: "";
        display: block;
        width: 1px;
        height: 15px;
        margin-left: 20px;
        background-color: #6b6b6b;
    }
}

.module-list {
    display: flex;
    color: $color-text-normal;

    >.module-item {
        height: $height-header;
        line-height: $height-header - 2;
        margin: 0 20px;
        cursor: pointer;
        border-bottom: solid 2px transparent;
        text-decoration: none;
        color: $color-text-normal;

        &:hover,
        &.active {
            color: $color-text-active;
            border-bottom-color: $color-text-active;
        }
    }
}
</style>