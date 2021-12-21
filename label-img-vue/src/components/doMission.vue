<template>
  <p>mission ID: {{ this.$route.params.ID }}</p>
  <div class="card" v-for="(image, index) in missionImages" :key="image.id">
    <el-image :src="image.url" :id="image.id" alt="labelIMG" ref="image"></el-image>
    {{ index }}
    <el-button @click="dialogVisible[index] = true">开始标注</el-button>
    <el-dialog v-model="dialogVisible[index]" fullscreen @opened="init(image.id)">
      <canvas :id="'canvas'+image.id"/>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "MissionDesc",
  data() {
    return {
      //data for mission display
      missionImages: [],
      missionState: '',
      //data for labeling
      dialogVisible: [],
      image: null,
      ctx: null,
    }
  },
  methods: {
    init(id) {
      console.log("id" + id)
      console.log(this.dialogVisible)
      let canvas = document.getElementById('canvas'+id)
      console.log(canvas)
      this.image = document.getElementById(id.toString())
      console.log(this.image)
      this.ctx = canvas.getContext('2d')
      console.log(this.ctx)
      this.ctx.drawImage(this.image, 0, 0, 100, 75)
    },
  },
  mounted() {
    axios.post("missionDesc/", {fromID: this.$route.params.ID}).then((success) => {
      this.missionImages = success.data
      console.log(this.missionImages)
      for (let i = 0; i < this.missionImages.length; i++
      ) {
        this.dialogVisible.push(false)
      }
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