<template>
  <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">

  <el-row gutter="20" class="row_col">
    <div class="card" v-for="(image, index) in missionImages" :key="image.id">
      <el-col>
        <el-image :src="image.url+'?'+new Date().getTime()" :fit="fit"
                  :id="image.id" alt="labelIMG" ref="image"
                  crossOrigin=""></el-image>
        <br>
        <el-button @click="dialogVisible[index] = true;getSingle(image.id)" :disabled="isFinish">开始标注</el-button>
        <el-dialog v-model="dialogVisible[index]" fullscreen @opened="init(image.id);">
          <h2>标注界面</h2>
          <canvas :id="'canvas'+image.id" @mousedown="handleMouseDown($event)"
                  @mousemove="handleMouseMove( $event)"
                  @mouseup="handleMouseUp($event) "/>
          <br>
          <div class="input" v-for="(txt,index) in counter" :key="index">
            {{ index }}
            <el-input style="width: 300px" :id="txt" type="text" v-model="remarks[index]" placeholder="标注备注" clearable/>
          </div>
          <el-button @click="reset">清空标注</el-button>
          <el-button @click="submitLabel(image.id);dialogVisible[index]=false">提交标注</el-button>
        </el-dialog>
      </el-col>
    </div>
  </el-row>
  <el-divider></el-divider>

  <el-button @click="submitMission(this.$route.params.ID)" :disabled="isFinish">提交任务</el-button>
  <br>
  <el-button @click="getPASCAL_VOC(this.$route.params.ID);seeVOC=true">导出PASCAL VOC</el-button>
  <el-dialog
      v-model="seeVOC"
      title="PASCAL VOC"
      fullscreen
  >
    <text class="content">
      <vue-code-highlight language="xml">
      <pre>
        {{ this.VOC }}
      </pre>
      </vue-code-highlight>
    </text>
    <el-button @click="seeVOC = false">Cancel</el-button>
    <el-button type="primary" @click="seeVOC = false"
    >Confirm
    </el-button>
  </el-dialog>
  <br>
  <el-button @click="getCOCO(this.$route.params.ID);seeCOCO=true">导出COCO</el-button>
  <el-dialog
      v-model="seeCOCO"
      title="COCO"
      width="30%"
  >
    <span class="content">
      <json-viewer :value="COCO"></json-viewer>
    </span>
    <span class="dialog-footer">
        <el-button @click="seeCOCO = false">Cancel</el-button>
        <el-button type="primary" @click="seeCOCO = false"
        >Confirm</el-button>
      </span>
  </el-dialog>
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
      //result set
      VOC: '',
      COCO: '',
      fit: 'contained',
      seeVOC: false,
      seeCOCO: false,
      isFinish: false,
    }
  },
  methods: {
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
      if (this.singleImage.pointsX !== null) {
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
      }
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
      this.$message.success("标注成功")
    },
    submitMission(missionID) {
      this.$message.success("提交成功")
      axios.post("submitMission/", {
        fromID: missionID
      }).then((success) => {
        console.log(success)
      }).catch((e) => {
        console.log(e)
      })
    },
    getPASCAL_VOC(missionID) {
      this.$message.success("导出成功")
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
      this.$message.success("导出成功")
      axios.post("getCOCO", {
        fromID: missionID
      }).then((success) => {
        this.VOC = null
        this.COCO = success.data
        console.log(success)
      }).catch((e) => {
        console.log(e)
      })
    },
    getSingle(imageID) {
      axios.post("singleImage", {x: this.$route.params.ID, y: imageID})
          .then((success) => this.singleImage = success.data)
          .catch((e) => console.log(e))
    },
    reset() {
      this.rects = []
      this.remarks = []
      this.counter = []
      this.draw(4)
    }
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

    axios.post("getSingleMission", {fromID: this.$route.params.ID}).then((success) => {
      this.missionState = success.data
      console.log(this.missionState)
      if (this.missionState.state === 3) {
        this.isFinish = true;
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
  padding: 40px;
}

.content {
  text-align: left;
}
</style>