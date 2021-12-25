<template>
  {{ this.$route.params.ID }}
  <el-button @click="goBack">返回任务列表</el-button>
  <el-button @click="submit">审核完成</el-button>

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
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "checkMission",
  data() {
    return {
      missionImages:[],
    }
  },
  methods: {
    goBack() {
      this.$router.replace('/managerIndex')
    },
    submit() {

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
  }
}
</script>

<style scoped>

</style>