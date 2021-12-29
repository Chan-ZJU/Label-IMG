<template>
  <br>
  <el-button @click="getPASCAL_VOC(this.$route.params.ID);seeVOC=true">导出PASCAL VOC</el-button>
  <el-dialog
      v-model="seeVOC"
      title="PASCAL VOC"
      fullscreen
  >
    <el-button @click="copyVOC">复制到剪切板</el-button>
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
    <el-button @click="copyCOCO">复制到剪切板</el-button>
    <span class="content">
      <json-viewer :value="COCO"></json-viewer>
    </span>
    <span class="dialog-footer">
        <el-button @click="seeCOCO = false">Cancel</el-button>
        <el-button type="primary" @click="seeCOCO = false"
        >Confirm</el-button>
      </span>
  </el-dialog>
  <el-divider></el-divider>
  <h2>图片集</h2>
  <el-row gutter="20" class="row_col">
    <div class="card" v-for="(image,index) in missionImages" :key="image.id">
      <el-col>
        <el-image :src="image.url+'?'+new Date().getTime()" :fit="fit"
                  :id="image.id" alt="labelIMG" ref="image"
                  crossOrigin="" style="width: 280px;height: 210px"></el-image>
        <br>
        <el-button @click="dialogVisible[index] = true;getSingle(image.id)">标注结果</el-button>
        <el-dialog v-model="dialogVisible[index]" fullscreen @opened="init(image.id);">
          <h2>标注结果</h2>
          <canvas :id="'canvas'+image.id"/>
          <br>
          <div class="input" v-for="(txt,index) in counter" :key="index">
            {{ index }}
            <el-input style="width: 300px" disabled :id="txt" type="text" v-model="remarks[index]" placeholder="标注备注"
                      clearable/>
          </div>
        </el-dialog>
      </el-col>
    </div>
    <el-divider></el-divider>
  </el-row>
</template>

<script>
import axios from "axios";
import useClipboard from 'vue-clipboard3'

export default {
  name: "MissionDesc",
  data() {
    return {
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
      seeCOCO: false
    }
  },
  methods: {
    init(id) {
      console.log("singleImage")
      console.log(this.singleImage)
      let canvas = document.getElementById('canvas' + id)
      console.log(canvas)
      this.image = document.getElementById(id.toString())
      console.log(this.image)
      canvas.width = this.image.width
      canvas.height = this.image.height
      this.width = this.image.width
      this.height = this.image.height
      this.naturalHeight = this.image.naturalHeight
      this.naturalWidth = this.image.naturalWidth
      this.ctx = canvas.getContext('2d')
      this.ctx.drawImage(this.image, 0, 0, this.width, this.height)

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
    getSingle(imageID) {
      axios.post("singleImage", {x: this.$route.params.ID, y: imageID})
          .then((success) => this.singleImage = success.data)
          .catch((e) => console.log(e))
    },
    copyVOC() {
      let {toClipboard} = useClipboard()
      try {
        toClipboard(this.VOC)
        this.$message.success("复制成功")
      } catch (e) {
        this.$message.error("复制错误")
      }
    },
    copyCOCO() {
      let {toClipboard} = useClipboard()
      try {
        toClipboard(JSON.stringify(this.COCO))
        this.$message.success("复制成功")
      } catch (e) {
        this.$message.error("复制错误")
      }
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
.content {
  text-align: left;
}
</style>