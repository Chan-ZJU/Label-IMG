<template>
<div>
  用户名:<input type="text" v-model="signupForm.username" placeholder="请输入用户名">
  密码:<input type="password" v-model="signupForm.password" placeholder="请输入密码">
  邮箱:<input type="email" v-model="signupForm.email" placeholder="请输入邮箱">
  <button v-on:click="signup">注册</button>
  <router-link to="/login">已有账号?点击登录</router-link>
</div>
</template>

<script>
import axios from "axios";

export default {
  name: "Signup",
  data(){
    return{
      signupForm:{
        username: '',
        password: '',
        email: ''
      },
      responseResult: []
    }
  },
  methods:{
    signup(){
      axios.post('/signup',{
        username: this.signupForm.username,
        password: this.signupForm.password,
        email: this.signupForm.email
      })
      .then((successResponse=>{
        console.log(successResponse)
        if(successResponse.data.code === 200){
          this.$router.replace('/')
        }
      }))
    }
  }
}
</script>

<style scoped>

</style>