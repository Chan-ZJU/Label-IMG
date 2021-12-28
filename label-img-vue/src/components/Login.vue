<template>
  <el-form class="login"
           status-icon
           label-width="120px"
           label-position="top"
  >
    <el-form-item label="用户名" required>
      <el-input type="text" v-model="loginForm.username" placeholder="请输入用户名" clearable>
      </el-input>
    </el-form-item>
    <el-form-item label="密码" required>
      <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" show-password>
      </el-input>
    </el-form-item>
    <el-form-item>
      <el-button v-on:click="login" style="text-align: center;">用户登录</el-button>
    </el-form-item>
    <el-form-item>
      <el-button v-on:click="managerLogin">管理员登陆</el-button>
    </el-form-item>
    <el-form-item>
      <router-link to="/signup">用户注册</router-link>
    </el-form-item>
  </el-form>
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
        } else {
          console.log(success)
        }
      }).catch((e) => {
        console.log(e)
      })
    }
  }
}

</script>
<style scoped>
.login {
  width: 450px;
  margin: 100px auto;
  padding: 20px;
}
</style>