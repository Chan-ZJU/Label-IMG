<template>
  <p>mission ID: {{ this.$route.params.ID }}</p>
  <div class="card" v-for="(image, index) in missionImages" :key="image.id">
    <el-image :src="image.url+'?'+new Date().getTime()" :id="image.id" alt="labelIMG" ref="image"
              crossOrigin=""></el-image>
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
      labelPoint: [],
    }
  },
  methods: {
    init(id, index) {
      let canvas = document.getElementById('canvas' + id)
      this.image[index] = document.getElementById(id.toString())
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
    },
    draw(type) {
      this.type = type
      this.ctx.lineWidth = 1
    },
    handleMouseDown(index, e) {
      if (this.type === 'square') {
        this.startPoint[index] = {
          x: e.offsetX,
          y: e.offsetY
        }
      }
      this.imageData[index] = this.ctx[index].getImageData(0, 0, this.width[index], this.height[index])
    },
    handleMouseMove(index, e) {
      if (!this.startPoint[index]) {
        return
      }
      this.ctx[index].beginPath()
      this.ctx[index].fillStyle = 'rgba(255,0,0,0.1)'
      this.ctx[index].strokeStyle = '#FF0000'
      this.ctx[index].strokeRect(this.startPoint[index].x, this.startPoint[index].y, e.offsetX - this.startPoint[index].x, e.offsetY - this.startPoint[index].y)
    },
    handleMouseUp(index, e) {
      console.log("index: " + index)
      console.log(e)
      if (!this.startPoint[index]) {
        console.log("error: no start point " + index)
      }
      if (this.type === 'square') {
        let point = []
        point.push({
          'point': [
            {
              x: this.startPoint[index].x,
              y: this.startPoint[index].y
            }, {
              x: e.offsetX,
              y: this.startPoint[index].y
            }, {
              x: e.offsetX,
              y: e.offsetY
            }, {
              x: this.startPoint[index].x,
              y: e.offsetY
            }
          ]
        })
        this.labelPoint[index] = point
        this.startPoint[index] = null
      }
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