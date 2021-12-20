<template>
  <p>mission ID: {{ this.$route.params.ID }}</p>
  <div class="card" v-for="(image) in missionImages" :key="image.id">
    <el-image :src="image.url"></el-image>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "MissionDesc",
  data() {
    return {
      missionImages: [],
      missionState: '',
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

</style>