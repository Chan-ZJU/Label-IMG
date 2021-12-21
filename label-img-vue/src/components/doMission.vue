<template>
  <p>mission ID: {{ this.$route.params.ID }}</p>
  <div class="card" v-for="(image, index) in missionImages" :key="image.id">
    <el-image :src="image.url+'?'+new Date().getTime()" :id="image.id" alt="labelIMG" ref="image"
              crossOrigin=""></el-image>
    <el-button @click="dialogVisible[index] = true">开始标注</el-button>
    <el-dialog v-model="dialogVisible[index]" fullscreen @opened="init(image.id, index)" @closed="type=null">
      <h2>标注界面</h2>
      <canvas :id="'canvas'+image.id" @click="handleClick(index, $event)" @mousedown="handleMouseDown(index, $event)"
              @mousemove="handleMouseMove(index, $event)"
              @mouseup="handleMouseUp(index, $event)" @contextmenu.prevent.stop="showMenu(index,$event)"/>
      <ul class="tips" v-show="isShowTips[index]">
        <li>
          <el-button size="small" @click="handleDelete(index)">删除</el-button>
        </li>
        <li>
          <el-button size="small" @click="handleNote(index)">备注</el-button>
        </li>
      </ul>
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
      isShowTips: [],
      //data for labeling
      selectID: null,
      alreadyInit: [],
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
      historyImageData: [],
    }
  },
  methods: {
    init(id, index) {
      if (this.alreadyInit[index] === true) {
        return
      } else {
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
        this.alreadyInit[index] = true
      }
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
      this.ctx[index].putImageData(this.imageData[index], 0, 0)
      this.ctx[index].beginPath()
      this.ctx[index].fillStyle = 'rgba(255,0,0,0.1)'
      this.ctx[index].strokeStyle = '#FF0000'
      this.ctx[index].strokeRect(this.startPoint[index].x, this.startPoint[index].y, e.offsetX - this.startPoint[index].x, e.offsetY - this.startPoint[index].y)
    },
    handleMouseUp(index, e) {
      if (!this.startPoint[index]) {
        console.log("error: no start point " + index)
      }
      if (this.type === 'square') {
        if (this.historyImageData[index] !== undefined) {
          this.historyImageData[index].push(this.ctx[index].getImageData(0, 0, this.width[index], this.height[index]))
        } else {
          this.historyImageData[index] = [this.ctx[index].getImageData(0, 0, this.width[index], this.height[index])]
        }
        let point = [
          {
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
          }
        ]
        if (this.labelPoint[index] !== undefined) {
          this.labelPoint[index].push(point[0])
        } else {
          this.labelPoint[index] = point
        }
        this.startPoint[index] = null
      }
    },
    handleClick(e) {
      console.log(e)
    },
    showMenu(index, e) {
      if (!this.labelPoint[index]) {
        return
      }
      this.type = null
      const isIn = this.labelPoint[index].findIndex((item) => {
        let leftX = false
        let rightX = false
        let topY = false
        let bottomY = false
        item.point.forEach(p => {
          if (e.offsetX > p.x) {
            leftX = true
          }
          if (e.offsetX < p.x) {
            rightX = true
          }
          if (e.offsetY < p.y) {
            bottomY = true
          }
          if (e.offsetY > p.y) {
            topY = true
          }
        })
        return leftX && rightX && topY && bottomY
      })
      if (isIn > -1) {
        this.selectID = isIn
        this.fillRect(index, this.labelPoint[index][isIn].point)
        this.isShowTips[index] = true
      }
    },
    fillRect(index, pointArray) {
      this.ctx[index].beginPath()
      this.ctx[index].fillStyle = 'rgba(255,0,0,0.1)'
      this.ctx[index].moveTo(pointArray[0].x, pointArray[0].y);
      for (let i = 1; i < pointArray.length; i++) {
        this.ctx[index].lineTo(pointArray[i].x, pointArray[i].y);
      }
      this.ctx[index].fill()
    },
    handleDelete(index) {
      this.ctx[index].putImageData(this.historyImageData[index][this.historyImageData[index].length - 1], 0, 0)
      this.ctx[index].beginPath()
      this.ctx[index].globalCompositeOperation = 'destination-out'
      const data = this.labelPoint[index][this.selectID].point
      this.ctx[index].lineWidth = 5
      this.ctx[index].moveTo(data[0].x - 3, data[0].y - 2)
      for (let i = 1; i < data.length; i++) {
        this.ctx[index].lineTo(data[i].x, data[i].y)
        this.ctx[index].stroke()
      }
      this.ctx[index].lineTo(data[0].x, data[0].y)
      this.ctx[index].stroke()
      this.labelPoint[index].splice(this.selectID, 1)
      this.historyImageData[index].push(this.ctx[index].getImageData(0, 0, this.w, this.h))
      this.isShowTips[index] = false
      this.ctx[index].globalCompositeOperation = 'source-over'
    },
  }
  ,
  mounted() {
    axios.post("missionDesc/", {fromID: this.$route.params.ID}).then((success) => {
      this.missionImages = success.data
      console.log(this.missionImages)
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