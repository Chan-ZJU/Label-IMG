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
      <div class="input" v-for="(txt,index) in counter" :key="index">
        {{ index }}
        <el-input :id="txt" type="text" v-model="remarks[index]" placeholder="标注备注"/>
      </div>
      <br>
      <el-button @click="submitLabel(image.id)">提交标注</el-button>
    </el-dialog>
  </div>
  <el-divider></el-divider>
  <el-button @click="submitMission(this.$route.params.ID)">提交任务</el-button>
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
  name: "doMissionWithCanvas",
  data() {
    return {
      //data for mission display
      missionImages: [],
      missionState: '',
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
      labelPoint: [],
      rects: [],
      clickOnPoint: false,
      counter: [],
      remarks: [],
      //result set
      VOC: '',
      COCO: '',
    }
  },
  methods: {
    init(id) {
      this.rects = []
      this.labelPoint = []
      this.counter = []
      this.remarks = []
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
    },
    draw(r) {
      console.log(this.rects)
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
        if (this.labelPoint !== undefined) {
          this.labelPoint.push(point[0])
        } else {
          this.labelPoint = point
        }
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
        if (this.labelPoint !== undefined) {
          this.labelPoint.push(point[0])
        } else {
          this.labelPoint = point
        }
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
    submitMission(missionID) {
      axios.post("submitMission/", {
        fromID: missionID
      }).then((success) => {
        console.log(success)
      }).catch((e) => {
        console.log(e)
      })
    },
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