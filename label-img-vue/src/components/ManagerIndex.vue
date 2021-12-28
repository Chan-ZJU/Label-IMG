<template>
  管理员主页
  <el-row gutter="20" class="card">
    <div class="card" v-for="(mission) in missions" :key="mission.id">
      <el-col>
        <el-card>
          <div v-if="mission.state===0">
            <el-tag type="danger">空闲</el-tag>
          </div>
          <div v-if="mission.state===1">
            <el-tag color="pink">任务中</el-tag>
          </div>
          <div v-if="mission.state===2">
            <el-tag type="warning">审核中</el-tag>
          </div>
          <div v-if="mission.state===3">
            <el-tag type="success">完成</el-tag>
          </div>
          <h4>任务描述: {{ mission.description }}</h4>
          <router-link :to="{name:'checkMission', params:{ID:mission.id}}">
            <el-tag>
              查看详情
            </el-tag>
          </router-link>
        </el-card>
      </el-col>
    </div>
  </el-row>
</template>

<script>
import axios from "axios";

export default {
  name: "ManagerIndex",
  data() {
    return {
      missions: [],
    }
  },
  mounted() {
    axios.post("getCheck").then((success) => {
      this.missions = success.data
    }).catch((e) => {
      console.log(e)
    })
  }
}
</script>

<style scoped>
.card {
  margin: auto;
  padding: 30px;
}
</style>