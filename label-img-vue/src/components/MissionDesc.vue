<template>
  <h3>任务详情</h3>
  <el-row gutter="20" class="row_col">
    <div class="card" v-for="(message) in missionImages" :key="message.id">
      <el-col>
        <el-image :fit="fit" :src="message.url" style="width: 560px;height: 420px"></el-image>
      </el-col>
    </div>
  </el-row>
  <el-button @click="claimMission" :disabled="canClaim">认领任务</el-button>
</template>
<script>
import axios from "axios";

export default {
  name: "MissionDesc",
  data() {
    return {
      missionImages: [],
      missionState: '',
      canClaim: false,
      fit: 'contain',
    }
  },
  methods: {
    claimMission() {
      axios.post("claimMission", {
        fromID: this.$store.state.user.userID,
        missionID: this.$route.params.ID
      }).then((success) => {
        console.log(success)
      }).catch((error) => {
        console.log(error)
      })
      this.$message.success("领取成功")
      this.canClaim = true
    }
  },
  mounted() {
    axios.post("missionDesc/", {fromID: this.$route.params.ID}).then((success) => {
      this.missionImages = success.data
      console.log(this.missionImages)
    }).catch((error) => {
      console.log(error)
    })
    axios.post("getSingleMission", {fromID: this.$route.params.ID}).then((success) => {
      this.missionState = success.data
      console.log(this.missionState)
      if (this.missionState.state === 1) {
        this.canClaim = true;
      }
    }).catch((error) => {
      console.log(error)
    })
  }
}
</script>

<style scoped>
.row_col {
  margin: auto;
  padding: 30px;
}
</style>