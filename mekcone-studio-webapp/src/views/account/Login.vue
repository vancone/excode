<template>
  <div class="login-page">
    <a-row>
      <!-- <a-col :span="6" style="padding: 20px;"> -->
      <a-layout-content style="text-align:center;">
        <a-card style="border-radius:5px;width:400px;margin:0 auto;vertical-align:middle;margin-top:calc(50% - 500px);">
          <h1>MekCone Studio</h1>
          <a-input style="margin-bottom:15px;" v-model="username"/>
          <a-input-password placeholder="Password" v-model="password" />
          <a-button type="primary" style="width:100%;margin-top:15px;" @click="login">
            Sign in
          </a-button>
        </a-card>
      </a-layout-content>
      <!-- </a-col> -->
    </a-row>
    <div class="footer">
      <p>Copyright &copy; 2020 MekCone. All rights reserved.</p>
    </div>
  </div>
</template>

<script>
// import GlobalHeader from '@/components/GlobalHeader'
import { login } from '@/api/user'
export default {
  name: 'Login',
  components: {
    // GlobalHeader
  },
  data () {
    return {
      username: '',
      password: '',
      columns: [
        {
          title: 'Name',
          dataIndex: 'name',
          key: 'name',
          scopedSlots: { customRender: 'name' }
        },
        {
          title: 'Modified Time',
          dataIndex: 'modifiedTime',
          key: 'modifiedTime',
          width: 200
        }
      ],
      data: []
    }
  },
  created () {
    // getProjectList().then((res) => {
    //   this.data = res.data.content
    // })
  },
  methods: {
    login () {
      login({ username: this.username, password: this.password }).then((res) => {
        // console.log(res)
        this.$router.push('/')
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
.footer {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  color: #999;
}
</style>
