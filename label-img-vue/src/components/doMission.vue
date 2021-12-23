<template>
  <p>mission ID: {{ this.$route.params.ID }}</p>
  <div class="card" v-for="(image, index) in missionImages" :key="image.id">
    <el-image :src="image.url+'?'+new Date().getTime()" :id="image.id" alt="labelIMG" ref="image"
              crossOrigin=""></el-image>
    <el-button @click="dialogVisible[index] = true">开始标注</el-button>
    <el-dialog v-model="dialogVisible[index]" fullscreen
               @opened="loadCanvas(index,image.id, image.url+'?'+new Date().toString())">
      <h2>标注界面</h2>
      <br>
      <div class="panel-body">
        <div class="demo">
          <canvas :id='"canvas"+image.id'
                  style="display: block; margin: 0 auto; border: 1px"></canvas>
          <div class="draw-btn-group">
            <el-button :class="{active:type===''}" title="自由选择" @click="drawTypeChange('')">
              自由选择
            </el-button>
            <el-button :class="{active:type==='text'}" title="文本输入框" @click="drawTypeChange('text')">
              标注备注
            </el-button>
            <el-button :class="{active:type==='round'}" title="画圆" @click="drawTypeChange('ellipse')">
              圆形
            </el-button>
            <el-button :class="{active:type==='square'}" title="画矩形" @click="drawTypeChange('rectangle')">
              矩形
            </el-button>
            <el-button :class="{active:type==='polygon'}" title="画多边形" @click="drawPolygon">
              多边形
            </el-button>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import {fabric} from "fabric";

export default {
  name: "doMission",
  data() {
    return {
      missionImages: [],
      missionState: '',
      dialogVisible: [],
      //type is square or dot
      type: null,
      canvas: [],
      naturalHeight: [],
      naturalWidth: [],
      width: [],
      height: [],
    }
  },
  methods: {
    loadCanvas(index, id) {
      let img = document.getElementById(id.toString())
      let initCanvas = document.getElementById("canvas" + id)

      initCanvas.width = img.width
      initCanvas.height = img.height
      console.log(initCanvas)
      console.log(initCanvas.width + " " + initCanvas.height)
      this.canvas[index] = new fabric.Canvas("canvas" + id)
      this.canvas[index].selectionColor = "rgba(0,0,0,0.05)"
      this.canvas[index].on("mouse:down", this.mousedown)
      this.canvas[index].on("mouse:move", this.mousemove)
      this.canvas[index].on("mouse:up", this.mouseup)
      let image = new Image(img.width, img.height)
      image.src = img.src
      let imageInstance = new fabric.Image(image, {selectable: false})
      this.canvas[index].add(imageInstance)
    },
  },
  mounted() {
    axios.post("missionDesc/", {fromID: this.$route.params.ID}).then((success) => {
      this.missionImages = success.data
      console.log(this.missionImages)
      this.canvas = new Array(this.missionImages.length)
      this.width = new Array(this.missionImages.length)
      this.height = new Array(this.missionImages.length)
      for (let i = 0; i < this.missionImages.length; i++) {
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