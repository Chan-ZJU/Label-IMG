<template>
  <p>我创建的任务</p>
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
          <router-link :to="{name:'pureDesc', params:{ID:mission.id}}">
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
  name: "MyMissions",
  data() {
    return {
      missions: [],
    }
  },
  methods: {},
  mounted() {
    let self = this
    let form = new FormData()
    form.append("userID", this.$store.state.user.userID)
    axios.post("myMission", form).then((successResponse) => {
      self.missions = successResponse.data
      console.log(this.missions)
    }).catch((error) => {
      console.log(error)
    })
  },
}
</script>

<style scoped>
.card {
  margin: auto;
  padding: 30px;
}
</style>