<template>
  <el-form
      class="signup"
      status-icon
      label-width="120px"
      label-position="top"
  >
    <el-form-item required label="用户名">
      <el-input type="text" v-model="signupForm.username" placeholder="请输入用户名" clearable></el-input>
    </el-form-item>
    <el-form-item required label="密码">
      <el-input type="password" v-model="signupForm.password" placeholder="请输入密码" show-password></el-input>
    </el-form-item>
    <el-form-item required label="邮箱">
      <el-input type="email" v-model="signupForm.email" placeholder="请输入邮箱" clearable></el-input>
    </el-form-item>
    <el-form-item>
      <el-button v-on:click="signup">注册</el-button>
    </el-form-item>
    <el-form-item>
      <router-link to="/login">已有账号?点击登录</router-link>
    </el-form-item>
  </el-form>
</template>

<script>
import axios from "axios";

export default {
  name: "Signup",
  data() {
    return {
      signupForm: {
        username: '',
        password: '',
        email: ''
      },
      responseResult: []
    }
  },
  methods: {
    signup() {
      axios.post('/signup', {
        username: this.signupForm.username,
        password: this.signupForm.password,
        email: this.signupForm.email
      })
          .then((successResponse => {
            console.log(successResponse)
            if (successResponse.data.code === 200) {
              this.$router.replace('/login')
            } else {
              this.$message.error("注册失败!")
            }
          })).catch((e) => {
        console.log(e)
        this.$message.error("注册失败 用户名6-50位, 密码6位以上")
      })
    }
  }
}
</script>

<style scoped>
.signup {
  width: 300px;
  margin: auto;
  padding: 20px;
}
</style>