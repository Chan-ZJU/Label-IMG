<template>
  <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
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
      pointNum: '',
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
      canvas.addEventListener("touchstart", this.handleTouchDown)
      canvas.addEventListener("touchmove", this.handleTouchMove)
      canvas.addEventListener("touchend", this.handleTouchUp)
    },
    draw() {
      console.log("draw")
      this.ctx.clearRect(0, 0, this.image.width, this.image.height)
      this.ctx.drawImage(this.image, 0, 0, this.width, this.height)
      if (this.rects !== undefined) {
        for (let i = 0; i < this.rects.length; i += 4) {
          this.ctx.strokeRect(this.rects[i]['x'], this.rects[i]['y'], this.rects[i + 2]['x'] - this.rects[i]['x'], this.rects[i + 2]['y'] - this.rects[i]['y'])
        }
      }
    },
    onPoint(x, y) {
      if (this.rects !== undefined) {
        let round = 5
        for (let i = 0; i < this.rects.length; i++) {
          // console.log(point)
          let point = this.rects[i]
          if (x - point['x'] <= round && x - point['x'] >= -round && y - point['y'] >= -round && y - point['y'] <= round) {
            return i
          }
          i++;
        }
      }
      return -1
    },
    handleMouseDown(e) {
      //点在点上，要进行拖拽
      this.pointNum = this.onPoint(e.offsetX, e.offsetY)
      this.clickOnPoint = false
      if (this.pointNum !== -1) {
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
    handleTouchDown(e) {
      //点在点上，要进行拖拽
      this.pointNum = this.onPoint(e.offsetX, e.offsetY)
      this.clickOnPoint = false
      if (this.pointNum !== -1) {
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
      if (!this.startPoint || this.clickOnPoint) {
        return
      }
      this.ctx.putImageData(this.imageData, 0, 0)
      this.ctx.beginPath()
      this.ctx.fillStyle = 'rgba(255,0,0,0.1)'
      this.ctx.strokeStyle = '#FF0000'
      this.ctx.strokeRect(this.startPoint.x, this.startPoint.y, e.offsetX - this.startPoint.x, e.offsetY - this.startPoint.y)
    },
    handleTouchMove(e) {
      if (!this.startPoint || this.clickOnPoint) {
        return
      }
      this.ctx.putImageData(this.imageData, 0, 0)
      this.ctx.beginPath()
      this.ctx.fillStyle = 'rgba(255,0,0,0.1)'
      this.ctx.strokeStyle = '#FF0000'
      this.ctx.strokeRect(this.startPoint.x, this.startPoint.y, e.offsetX - this.startPoint.x, e.offsetY - this.startPoint.y)
    },
    handleMouseUp(e) {
      console.log(e)
      if (!this.startPoint) {
        console.log("error: no start point ")
      }
      if (!this.clickOnPoint) {
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
      } else {
        //点到了点上，开始拖拽
        let num = this.pointNum % 4
        this.rects[this.pointNum]['x'] = e.offsetX
        this.rects[this.pointNum]['y'] = e.offsetY
        if (num === 0 || num === 2) {
          this.rects[this.pointNum - 1 > 0 ? this.pointNum - 1 : this.pointNum + 3]['x'] = e.offsetX
          this.rects[this.pointNum + 1]['y'] = e.offsetY
        } else {
          this.rects[this.pointNum + 1 > 3 ? this.pointNum - 3 : this.pointNum + 1]['x'] = e.offsetX
          this.rects[this.pointNum - 1]['y'] = e.offsetY
        }
      }
      this.draw()
    },
    handleTouchUp(e) {
      console.log(e)
      if (!this.startPoint) {
        console.log("error: no start point ")
      }
      if (!this.clickOnPoint) {
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
      } else {
        //点到了点上，开始拖拽
        let num = this.pointNum % 4
        this.rects[this.pointNum]['x'] = e.offsetX
        this.rects[this.pointNum]['y'] = e.offsetY
        if (num === 0 || num === 2) {
          this.rects[this.pointNum - 1 > 0 ? this.pointNum - 1 : this.pointNum + 3]['x'] = e.offsetX
          this.rects[this.pointNum + 1]['y'] = e.offsetY
        } else {
          this.rects[this.pointNum + 1 > 3 ? this.pointNum - 3 : this.pointNum + 1]['x'] = e.offsetX
          this.rects[this.pointNum - 1]['y'] = e.offsetY
        }
      }
      this.draw()
    }
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