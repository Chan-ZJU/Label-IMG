<template>
  <p>mission ID: {{ this.$route.params.ID }}</p>
  <div class="card" v-for="(image, index) in missionImages" :key="image.id">
    <el-image :src="image.url" :id="image.id" alt="labelIMG" ref="image"></el-image>
    {{ index }}
    <el-button @click="dialogVisible[index] = true">开始标注</el-button>
    <el-dialog v-model="dialogVisible[index]" fullscreen @opened="init(image.id, index)" @closed="type=null">
      <h2>标注界面</h2>
      <canvas :id="'canvas'+image.id" @click="handleClick(index, $event)" @mousedown="handleMouseDown(index, $event)"
              @mousemove="handleMouseMove(index, $event)"
              @mouseup="handleMouseUp(index, $event)"/>
      <br>
      <el-button @click="draw('square')">矩形标注</el-button>
      <el-button @click="draw('dot')">打点标注</el-button>
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
      image: [],
      ctx: [],
      naturalHeight: [],
      naturalWidth: [],
      width: [],
      height: [],
      imageData: [],
      type: null,
      startPoint: [],
    }
  },
  methods: {
    init(id, index) {
      let canvas = document.getElementById('canvas' + id)
      this.image[index] = document.getElementById(id.toString())
      this.image[index].crossOrigin = '';
      console.log("this.image")
      console.log(this.image)
      canvas.width = this.image[index].width
      canvas.height = this.image[index].height
      this.width[index] = this.image[index].width
      this.height[index] = this.image[index].height
      this.naturalHeight[index] = this.image[index].naturalHeight
      this.naturalWidth[index] = this.image[index].naturalWidth
      console.log(this.height[index] + " " + this.width[index])
      console.log(this.naturalHeight[index] + " " + this.naturalWidth[index])
      this.ctx[index] = canvas.getContext('2d')
      this.ctx[index].drawImage(this.image[index], 0, 0, this.width[index], this.height[index])
      this.ctx[index].getImageData(0, 0, this.width[index], this.height[index])
    },
    draw(type) {
      this.type = type
      this.ctx.lineWidth = 1
    },
    handleMouseDown(index, e) {
      console.log("index: " + index)
      console.log(e)
      if (this.type === 'square') {
        this.startPoint[index] = {
          x: e.offsetX,
          y: e.offsetY
        }
      }
      console.log(this.startPoint[index])
    },
    handleMouseMove(index, e) {
      console.log("index: " + index)
      console.log(e)
      if (!this.startPoint[index]) {
        console.log("no startPoint " + index)
      }
    },
    handleMouseUp(index, e) {
      console.log("index: " + index)
      console.log(e)
    },
    handleClick(e) {
      console.log(e)
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