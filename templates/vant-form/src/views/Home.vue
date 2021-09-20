<template>
  <div class="home">
    <van-config-provider :theme-vars="themeVars">
      <van-form @submit="onSubmit">
        <van-cell-group inset>
          <van-field
            v-model="username"
            name="用户名"
            label="用户名"
            placeholder="用户名"
            :rules="[{ required: true, message: '请填写用户名' }]"
          />
          <van-field
            v-model="password"
            type="password"
            name="密码"
            label="密码"
            placeholder="密码"
            :rules="[{ required: true, message: '请填写密码' }]"
          />
        </van-cell-group>
        <div style="margin: 16px">
          <van-button round block type="primary" @click="submit">
            提交
          </van-button>
        </div>
      </van-form>
    </van-config-provider>
  </div>
</template>

<script>
import router from '@/router/index.js'
export default {
  name: 'Home',
  data: () => {
    return {
      username: '',
      password: ''
    }
  },
  setup () {
    const themeVars = {
      rateIconFullColor: '#07c160',
      sliderBarHeight: '4px',
      sliderButtonWidth: '20px',
      sliderButtonHeight: '20px',
      sliderActiveBackgroundColor: '#07c160',
      // buttonPrimaryBorderColor: '#07c160',
      // buttonPrimaryBackgroundColor: '#07c160',
      navBarHeight: '60px',
      navBarBackgroundColor: '#000',
      navBarTitleTextColor: '#fff'
    }

    function submit () {
      router.push('/success')
    }

    return {
      submit,
      themeVars
    }
  }
}
</script>

<style>
</style>
