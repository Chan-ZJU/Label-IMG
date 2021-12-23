<template>
  <p>mission ID: {{ this.$route.params.ID }}</p>
  <div class="card" v-for="(image, index) in missionImages" :key="image.id">
    <el-image :src="image.url+'?'+new Date().getTime()" :id="image.id" alt="labelIMG" ref="image"
              crossOrigin=""></el-image>
    <el-button @click="dialogVisible[index] = true">开始标注</el-button>
    <el-dialog v-model="dialogVisible[index]" fullscreen @opened="init(image.id);">
      <h2>标注界面</h2>
      <canvas :id="'canvas'+image.id" @mousedown="handleMouseDown($event)"
              @mousemove="handleMouseMove( $event)"
              @mouseup="handleMouseUp($event) "/>
      <br>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "doMissionWithCanvas",
  data() {
    return {
      //data for mission display
      missionImages: [],
      missionState: '',
      isShowTips: [],
      //data for labeling
      selectID: null,
      alreadyInit: [],
      dialogVisible: [],
      image: '',
      ctx: '',
      naturalHeight: '',
      naturalWidth: '',
      width: '',
      height: '',
      imageData: '',
      startPoint: '',
      labelPoint: [],
      rects: [],
      clickOnPoint: false,
    }
  },
  methods: {
    init(id) {
      this.rects = []
      this.labelPoint = []
      let canvas = document.getElementById('canvas' + id)
      this.image = document.getElementById(id.toString())
      canvas.width = this.image.width
      canvas.height = this.image.height
      this.width = this.image.width
      this.height = this.image.height
      this.naturalHeight = this.image.naturalHeight
      this.naturalWidth = this.image.naturalWidth
      this.ctx = canvas.getContext('2d')
      this.ctx.drawImage(this.image, 0, 0, this.width, this.height)
      this.alreadyInit = true
    },
    draw() {
      this.ctx.clearRect(0, 0, this.image.width, this.image.height)
      this.ctx.drawImage(this.image, 0, 0, this.width, this.height)
      if (this.rects !== undefined) {
        for (let i = 0; i < this.rects.length; i += 4) {
          this.ctx.strokeRect(this.rects[i]['x'], this.rects[i]['y'], this.rects[i + 2]['x'] - this.rects[i]['x'], this.rects[i + 2]['y'] - this.rects[i]['y'])
        }
      }
    },
    onPoint(x, y) {
      console.log("onPoint")
      let i = 0
      for (let point in this.rects) {
        if (x - point['x'] <= 2 && x - point['x'] >= -2 && y - point['y'] >= -2 && y - point['y'] <= 2)
          return i
        i++;
      }
      return -1
    },
    handleMouseDown(e) {
      //点在点上，要进行拖拽
      let pointNum = 0
      this.clickOnPoint = false
      if (this.onPoint(e.offsetX, e.offsetY) !== -1) {
        this.clickOnPoint = true
        // this.startPoint[index] = null
      } else {
        this.startPoint = {
          x: e.offsetX,
          y: e.offsetY
        }
        this.imageData = this.ctx.getImageData(0, 0, this.width, this.height)
      }
    },
    handleMouseMove(e) {
      if (!this.startPoint && this.clickOnPoint) {
        return
      }
      console.log(123)
      this.ctx.putImageData(this.imageData, 0, 0)
      this.ctx.beginPath()
      this.ctx.fillStyle = 'rgba(255,0,0,0.1)'
      this.ctx.strokeStyle = '#FF0000'
      this.ctx.strokeRect(this.startPoint.x, this.startPoint.y, e.offsetX - this.startPoint.x, e.offsetY - this.startPoint.y)
    },
    handleMouseUp(e) {
      if (!this.startPoint) {
        console.log("error: no start point ")
      }
      let point = [
        {
          x: this.startPoint.x,
          y: this.startPoint.y
        }, {
          x: e.offsetX,
          y: this.startPoint.y
        }, {
          x: e.offsetX,
          y: e.offsetY
        }, {
          x: this.startPoint.x,
          y: e.offsetY
        }
      ]
      if (this.labelPoint !== undefined) {
        this.labelPoint.push(point[0])
      } else {
        this.labelPoint = point
      }
      for (let i = 0; i < 4; i++) {
        this.rects.push(point[i])
      }
      console.log(this.rects)
      this.startPoint = null
      this.draw()
    },
  },
  mounted() {
    axios.post("missionDesc/", {fromID: this.$route.params.ID}).then((success) => {
      this.missionImages = success.data
      for (let i = 0; i < this.missionImages.length; i++
      ) {
        this.dialogVisible.push(false)
        this.alreadyInit.push(false)
        this.isShowTips.push(false)
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