<template>
  <p>mission ID: {{ this.$route.params.ID }}</p>
  <div class="card" v-for="(image) in missionImages" :key="image.id">
    <el-image :src="image.url"></el-image>
  </div>
  <br>
  <el-button @click="getPASCAL_VOC(this.$route.params.ID)">导出PASCAL VOC</el-button>
  <br>
  <el-button @click="getCOCO(this.$route.params.ID)">导出COCO</el-button>
  <div style="white-space: pre-line;">{{ this.VOC }}</div>
  <div style="white-space: pre-line;">{{ this.COCO }}</div>
</template>

<script>
import axios from "axios";

export default {
  name: "MissionDesc",
  data() {
    return {
      missionImages: [],
      missionState: '',
      //result set
      VOC: '',
      COCO: '',
    }
  },
  methods: {
    getPASCAL_VOC(missionID) {
      axios.post("getPASCALVOC", {
        fromID: missionID
      }).then((success) => {
        console.log(success)
        this.COCO = null
        this.VOC = success.data
      }).catch((e) => {
        console.log(e)
      })
    },
    getCOCO(missionID) {
      axios.post("getCOCO", {
        fromID: missionID
      }).then((success) => {
        this.VOC = null
        this.COCO = success.data
        console.log(success)
      }).catch((e) => {
        console.log(e)
      })
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