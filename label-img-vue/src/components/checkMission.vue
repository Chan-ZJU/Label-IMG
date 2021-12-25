<template>
  {{ this.$route.params.ID }}
  <el-button @click="goBack">返回任务列表</el-button>
  <div class="card" v-for="(image, index) in missionImages" :key="image.id">
    <el-image :src="image.url+'?'+new Date().getTime()" :id="image.id" alt="labelIMG" ref="image"
              crossOrigin=""></el-image>
    <el-button @click="dialogVisible[index] = true;getSingle(image.id)">开始标注</el-button>
    <el-dialog v-model="dialogVisible[index]" fullscreen @opened="init(image.id);">
      <h2>审核界面</h2>
      <canvas :id="'canvas'+image.id" @mousedown="handleMouseDown($event)"
              @mousemove="handleMouseMove( $event)"
              @mouseup="handleMouseUp($event) "/>
      <br>
      <div class="input" v-for="(txt,index) in counter" :key="index">
        {{ index }}
        <el-input :id="txt" type="text" v-model="remarks[index]" placeholder="标注备注"/>
      </div>
      <el-button @click="submitLabel(image.id)">审核完成</el-button>
      <br>
    </el-dialog>
  </div>
  <el-button @click="submit">完成任务</el-button>
</template>

<script>
import axios from "axios";

export default {
  name: "checkMission",
  data() {
    return {
      missionImages: '',
      singleImage: '',
      //data for labeling
      selectID: null,
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
      rects: [],
      clickOnPoint: false,
      counter: [],
      remarks: [],
    }
  },
  methods: {
    goBack() {
      this.$router.replace('/managerIndex')
    },
    submit() {
      axios.post("finishMission", {fromID: this.$route.params.ID})
          .then((success) => console.log(success))
          .catch((e) => {
            console.log(e)
          })
    },
    init(id) {
      console.log("singleImage")
      console.log(this.singleImage)
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
      canvas.addEventListener("touchstart", this.handleTouchDown)
      canvas.addEventListener("touchmove", this.handleTouchMove)
      canvas.addEventListener("touchend", this.handleTouchUp)

      this.rects = []
      this.counter = []
      this.remarks = []
      //get previous label points
      let pointsX = this.singleImage.pointsX.split(",")
      let pointsY = this.singleImage.pointsY.split(",")
      for (let i = 0; i < pointsX.length - 1; i++) {
        let Point = {
          x: Math.ceil((parseInt(pointsX[i]) / this.naturalWidth) * this.width),
          y: Math.ceil((parseInt(pointsY[i]) / this.naturalHeight) * this.height)
        }
        this.rects.push(Point)
      }
      let remarkArray = this.singleImage.remarks.split(",")
      for (let i = 0; i < remarkArray.length - 1; i++) {
        this.remarks.push(remarkArray[i])
        this.counter.push(i)
      }
      this.draw(3)
    },
    draw(r) {
      console.log(this.rects)
      this.ctx.fillStyle = 'rgba(255,0,0,0.1)'
      this.ctx.strokeStyle = '#FF0000'
      this.ctx.clearRect(0, 0, this.image.width, this.image.height)
      this.ctx.drawImage(this.image, 0, 0, this.width, this.height)
      if (this.rects !== undefined) {
        for (let i = 0; i < this.rects.length; i += 4) {
          for (let j = 0; j < 4; j++) {
            this.ctx.strokeRect(this.rects[i + j]['x'] - r, this.rects[j + i]['y'] - r, 2 * r, 2 * r);
          }
          this.ctx.font = "30px"
          this.ctx.fillStyle = "red"
          this.ctx.fillText((i / 4).toString(), this.rects[i]['x'] - 5, this.rects[i]['y'] - 5)
          this.ctx.strokeRect(this.rects[i]['x'], this.rects[i]['y'], this.rects[i + 2]['x'] - this.rects[i]['x'], this.rects[i + 2]['y'] - this.rects[i]['y'])
        }
      }
    },
    onPoint(x, y, r) {
      if (this.rects !== undefined) {
        //手机端和电脑端r要不一样，手机端可以大一点
        let round = r
        for (let i = 0; i < this.rects.length; i++) {
          let point = this.rects[i]
          if (x - point['x'] <= round && x - point['x'] >= -round && y - point['y'] >= -round && y - point['y'] <= round) {
            return i
          }
        }
      }
      return -1
    },
    handleMouseDown(e) {
      //点在点上，要进行拖拽
      this.pointNum = this.onPoint(e.offsetX, e.offsetY, 9)
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
      //Convert clientX to offsetX
      let rec = e.target.getBoundingClientRect();
      let x = e.targetTouches[0].clientX - rec.left;
      let y = e.targetTouches[0].clientY - rec.top;
      this.pointNum = this.onPoint(x, y, 9)
      this.clickOnPoint = false
      if (this.pointNum !== -1) {
        this.clickOnPoint = true
        // this.startPoint[index] = null
      } else {
        this.startPoint = {
          x: x,
          y: y
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
      let rec = e.target.getBoundingClientRect();
      let x = e.targetTouches[0].clientX - rec.left;
      let y = e.targetTouches[0].clientY - rec.top;
      if (!this.startPoint || this.clickOnPoint) {
        return
      }
      this.ctx.putImageData(this.imageData, 0, 0)
      this.ctx.beginPath()
      this.ctx.fillStyle = 'rgba(255,0,0,0.1)'
      this.ctx.strokeStyle = '#FF0000'
      this.ctx.strokeRect(this.startPoint.x, this.startPoint.y, x - this.startPoint.x, y - this.startPoint.y)
    },
    handleMouseUp(e) {
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
        for (let i = 0; i < 4; i++) {
          this.rects.push(point[i])
        }
        this.startPoint = null
        this.counter.push(this.counter.length)
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
      this.draw(4)
    },
    handleTouchUp(e) {
      let rec = e.target.getBoundingClientRect();
      let x = e.changedTouches[0].clientX - rec.left;
      let y = e.changedTouches[0].clientY - rec.top;
      if (!this.startPoint) {
        console.log("error: no start point ")
      }
      if (!this.clickOnPoint) {
        let point = [
          {
            x: this.startPoint.x,
            y: this.startPoint.y
          }, {
            x: x,
            y: this.startPoint.y
          }, {
            x: x,
            y: y
          }, {
            x: this.startPoint.x,
            y: y
          }
        ]
        for (let i = 0; i < 4; i++) {
          this.rects.push(point[i])
        }
        this.startPoint = null
        this.counter.push(this.counter.length)
      } else {
        //点到了点上，开始拖拽
        let num = this.pointNum % 4
        this.rects[this.pointNum]['x'] = x
        this.rects[this.pointNum]['y'] = y
        if (num === 0 || num === 2) {
          this.rects[this.pointNum - 1 > 0 ? this.pointNum - 1 : this.pointNum + 3]['x'] = x
          this.rects[this.pointNum + 1]['y'] = y
        } else {
          this.rects[this.pointNum + 1 > 3 ? this.pointNum - 3 : this.pointNum + 1]['x'] = x
          this.rects[this.pointNum - 1]['y'] = y
        }
      }
      this.draw(3)
    },
    getSingle(imageID) {
      axios.post("singleImage", {x: this.$route.params.ID, y: imageID})
          .then((success) => this.singleImage = success.data)
          .catch((e) => console.log(e))
    },
    submitLabel(id) {
      console.log("natural: " + this.naturalWidth + " " + this.naturalHeight)
      console.log(this.width + " " + this.height)
      axios.post("submitLabel/", {
        missionID: this.$route.params.ID,
        points: this.rects,
        imageID: id,
        userID: this.$store.state.user.userID,
        remark: this.remarks,
        naturalSize: {x: this.naturalWidth, y: this.naturalHeight},
        ratio: {x: this.naturalWidth / this.width, y: this.naturalHeight / this.height}
      }).then((success) => {
        console.log(success)
      }).catch((e) => {
        console.log(e)
      })
    },
  },
  mounted() {
    axios.post("missionDesc/", {fromID: this.$route.params.ID}).then((success) => {
      this.missionImages = success.data
      for (let i = 0; i < this.missionImages.length; i++) {
        this.dialogVisible.push(false)
      }
    }).catch((error) => {
      console.log(error)
    })
  }
}
</script>

<style scoped>

</style>