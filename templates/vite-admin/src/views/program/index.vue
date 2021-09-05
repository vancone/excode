<template>
    <el-table
        class="table__colored-header"
        style="width: 100%"
        border
        :data="tableData"
    >
        <el-table-column prop="id" label="程序编号" fixed width="150" />
        <el-table-column prop="name" label="程序名称" fixed />
        <el-table-column prop="lang" label="编程语言" fixed width="100" />
        <el-table-column prop="creator" label="创建者" fixed width="100" />
        <el-table-column label="操作" fixed="right" width="120">
            <template #default="scope">
                <el-button type="text" size="small">运行</el-button>
                <el-button type="text" size="small">编辑</el-button>
                <el-button type="text" size="small">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
</template>

<script lang="ts">
import { defineComponent, reactive, toRefs, watch } from 'vue'
import { getSummaries, generateServiceName } from '@/utils/index'
import { IAnyObject } from '@/api/types'

export default defineComponent({
    setup() {
        const tableData = [
            {
                id: 'PYX2021073100001',
                name: 'Hello World',
                lang: 'Python',
                creator: 'admin',
            },
            {
                id: 'CXX2021073100002',
                name: 'Fibonacci sequence',
                lang: 'C'
            },
            {
                id: 'JAV2021073100003',
                name: 'Fibonacci sequence',
                lang: 'Java (Maven)'
            }
        ]

        return {
            tableData
        }
    },
})
</script>


<style lang="scss" scoped>
.block {
    border-radius: 5px;
    background-color: white;
    height: 100px;
    width: 200px;
    padding: 10px;
}
</style>