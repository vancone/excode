<template>
  <div class="login-page">
    <a-card class="login-panel">
      <img src="../../assets/logo.svg" class="logo">
      <h1>MekCone Studio</h1>
      <a-input class="username-textbox" placeholder="Username" size="large" v-model="username"/>
      <a-input-password size="large" placeholder="Password" v-model="password" />
      <a-button type="primary" size="large" class="button-login" @click="login">
        Sign in
      </a-button>
    </a-card>
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
.login-page {
  /* background: #1e1e1e; */
  height: 100%;
  width: 100%;
}
.login-panel {
  background: #3c3f41;
  border: none;
  border-radius:5px;
  width:400px;
  height:400px;
  margin:auto;
  position:absolute;
  top:0;
  bottom:0;
  left:0;
  right:0;
}
.login-panel .logo {
  height: 80px;
}
.login-panel h1 {
  font-weight: 500;
  font-size: 18px;
  margin-top: 10px;
  margin-bottom: 20px;
  color: #bbb;
}
.footer {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  color: #999;
}
.button-login {
  background: #365880;
  width: 100%;
  margin-top: 15px;
  border: solid 1px #4c708c;
  color: #bbb;
}
.username-textbox {
  margin-bottom: 15px;
  background: #45494a;
  border-color: #646464;
  color: #ddd;
}
/deep/ .ant-input-password input {
  background: #45494a;
  border: solid 1px #646464;
  color: #bbb;
}
/deep/ .ant-input-password-icon {
  color: #bbb;
}
</style>
