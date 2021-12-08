<template>
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
    <el-button @click="screenShot">截图</el-button>
    <div id="photos"></div>
  </el-dialog>
  <img :src="imgSrc">
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
      imgSrc: ''
    }
  },
  methods: {
    beforeUpload(file) {
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
    screenShot() {
      let video = document.getElementById("video")
      let photos = document.getElementById("photos")
      let canvas = this.createThumbnail(video, 0.25)
      canvas.onclick = function () {
        window.open(this.toDataURL());
      };
      this.listThumbnails.unshift(canvas);
      photos.innerHTML = "";
      this.listThumbnails.forEach((item) => {
        if (item) {
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

      let form = new FormData();
      form.append("file", fileOfBlob)
      form.append("ID", this.$store.state.user.userID)
      form.append("type", 0)

      axios.post("/ossUpload", form, {headers: {'Content-Type': 'multipart/form-data'}}).then(successResponse => {
        console.log(successResponse.data)
      }).catch(error => {
        console.log(error)
      })

      return canvas;
    }
  }
}
</script>

<style scoped>

</style>