<template>
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
  <el-divider></el-divider>
  <h2>图片集</h2>
  <el-row gutter="20" class="row_col">
    <div class="card" v-for="(message) in missionImages" :key="message.id">
      <el-col>
        <el-image :fit="fit" :src="message.url" style="width: 280px;height: 210px"></el-image>
      </el-col>
    </div>
    <el-divider></el-divider>
  </el-row>
</template>

<script>
import axios from "axios";

export default {
  name: "MissionDesc",
  data() {
    return {
      missionImages: [],
      missionState: '',
      //result set
      VOC: '',
      COCO: '',
      fit: 'contained',
      seeVOC: false,
      seeCOCO: false
    }
  },
  methods: {
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