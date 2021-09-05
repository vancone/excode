<template>
    <div id="building"></div>
    <p>页面建设中，敬请期待...</p>
</template>

<script>
import { defineComponent } from 'vue'

export default defineComponent({
    name: 'Building'
})
</script>

<style lang="scss" scoped>
#building {
    width: 100%;
    height: 150px;
    padding-top: 290px;
    margin-top: 60px;
    margin-bottom: 10px;
    text-align: center;
    vertical-align: bottom;
    background: url(@/assets/construction.svg) center center / 600px 400px no-repeat;
}

p {
    font-size: 18px;
    text-align: center;
}
</style>