<template>
  <p>上传视频</p>
  <el-upload
      class="upload"
      ref="upload"
      :data="user"
      drag
      action="http://localhost:8081/api/upload"
      multiple
      show-file-list
      :on-preview="handlePreview"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :before-remove="beforeRemove"
      :on-success="handleSuccess"
      :file-list="fileList"
  >
    <el-icon class="el-icon--upload">
      <upload-filled/>
    </el-icon>
    <div class="el-upload__text">
      Drop file here or <em>click to upload</em>
    </div>
    <template #tip>
      <div class="el-upload__tip">
        jpg/png files with a size less than 500kb
      </div>
    </template>
  </el-upload>
  <el-dialog v-model="dialogVisible">
    <video id="video" width=400 :src=dialogVideoUrl controls alt=""></video>
    <br>
    <el-button @click="screenShot">点击截图</el-button>
    <el-form :inline="true">
      <el-form-item>
        <el-input v-model="srcInterval" placeholder="请输入截图间隔(ms)">截图间隔</el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="srcNum" placeholder="请输入截图数量">截图张数</el-input>
      </el-form-item>
      <el-button @click="autoScreenShot">自动截图</el-button>
    </el-form>
    <div id="photos"></div>
  </el-dialog>
  <br>
</template>

<script setup>
import {UploadFilled} from '@element-plus/icons'
</script>

<script>
import axios from "axios";

export default {
  name: "UploadVideo",
  data() {
    return {
      listThumbnails: [],
      fileList: [],
      url: '',
      //type: 1 is video
      user: {ID: this.$store.state.user.userID, type: 1},
      dialogVisible: false,
      dialogVideoUrl: '',
      srcInterval: 1000,
      srcNum: 3
    }
  },
  methods: {
    beforeUpload(file) {
      console.log(this.fileList)
      if (this.fileList.length > 0) {
        this.fileList = []
      }
      console.log(file.raw)
    },
    handlePreview(file) {
      console.log(file)
      console.log(file.response)
      this.dialogVideoUrl = file.response
      this.dialogVisible = true
    },
    handleRemove(file) {
      console.log(file)
    },
    beforeRemove(file) {
      return this.$confirm(`确认删除${file.name}?`)
    },
    handleSuccess(response) {
      console.log(response)
      this.url = response
      // this.$emit('onUpload')
    },
    autoScreenShot() {
      document.querySelector('video').play()
      for (let i = 0; i < this.srcNum; i++) {
        setTimeout(this.screenShot, this.srcInterval * i)
      }
    },
    screenShot() {
      let video = document.getElementById("video")
      video.setAttribute('crossOrigin', 'anonymous')
      let photos = document.getElementById("photos")
      let canvas = this.createThumbnail(video, 1)
      this.listThumbnails.unshift(canvas);
      photos.innerHTML = "";
      console.log("photos: " + photos)
      this.listThumbnails.forEach((item) => {
        if (item) {
          item.setAttribute("style","width:100px;height:75px")
          photos.appendChild(item);
        }
      });
    },
    createThumbnail(video, scaleFactor) {
      if (scaleFactor == null) {
        scaleFactor = 1;
      }
      let w = video.videoWidth * scaleFactor;
      let h = video.videoHeight * scaleFactor;
      let canvas = document.createElement("canvas");
      canvas.style.margin = "5px";
      canvas.width = w;
      canvas.height = h;

      let ctx = canvas.getContext("2d");
      ctx.drawImage(video, 0, 0, w, h);

      let data = canvas.toDataURL('image/png')
      data = data.split(',')[1]
      data = window.atob(data)
      let ia = new Uint8Array(data.length)
      for (let i = 0; i < data.length; i++) {
        ia[i] = data.charCodeAt(i)
      }
      let blob = new Blob([ia], {type: 'image/png'})
      let fileName = 'photo.png'
      let fileOfBlob = new window.File([blob], fileName, {type: 'image/png'})
      console.log(fileOfBlob)
      this.uploadScreenshot(fileOfBlob)
      return canvas;
    },
    uploadScreenshot(fileOfBlob) {
      let form = new FormData();
      form.append("file", fileOfBlob)
      form.append("ID", this.$store.state.user.userID)
      form.append("type", 0)

      axios.post("/ossUpload", form, {headers: {'Content-Type': 'multipart/form-data'}}).then(successResponse => {
        console.log(successResponse.data)
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
</script>

<style scoped>

</style>