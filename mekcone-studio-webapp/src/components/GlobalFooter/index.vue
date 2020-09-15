<template>
  <global-footer class="footer custom-render">
    <template v-slot:links>
      <a href="https://studio.mekcone.com" target="_blank">MekCone Studio v0.0.1</a>
    </template>
    <template v-slot:copyright>
      <a href="https://www.mekcone.com" target="_blank">Copyright &nbsp; 2020 MekCone. All rights reserved.</a>
    </template>
  </global-footer>
</template>

<script>
import { GlobalFooter } from '@ant-design-vue/pro-layout'

export default {
  name: 'ProGlobalFooter',
  components: {
    GlobalFooter
  }
}
</script>
