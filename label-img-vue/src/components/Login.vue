<template>
  <el-card>
    用户名:<input type="text" v-model="loginForm.username" placeholder="请输入用户名">
    <br>
    密码:<input type="password" v-model="loginForm.password" placeholder="请输入密码">
    <br>
    <button v-on:click="login">用户登录</button>
    <br>
    <button v-on:click="managerLogin">管理员登陆</button>
    <br>
    <router-link to="/signup">用户注册</router-link>
    <br>
  </el-card>
</template>

<script>

import axios from "axios";

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        userID: ''
      },
      responseResult: []
    }
  }
  ,
  methods: {
    login() {
      let _this = this
      axios.post('/login', {
        username: this.loginForm.username,
        password: this.loginForm.password
      })
          .then(successResponse => {
            console.log(successResponse)
            if (successResponse.data.code === 200) {
              _this.loginForm.userID = successResponse.data.userID
              _this.$store.commit('login', _this.loginForm)
              let path = this.$route.query.redirect
              this.$router.replace({path: path === '/' || path === undefined ? '/index' : path})
            }
          })
          // eslint-disable-next-line no-unused-vars
          .catch(failResponse => {
          })
    },
    managerLogin() {
      axios.post("managerLogin", {
        username: this.loginForm.username,
        password: this.loginForm.password
      }).then((success) => {
        if (success.data === 200) {
          this.$router.replace('/managerIndex')
        }
      }).catch((e) => {
        console.log(e)
      })
    }
  }
}

</script>