<template>
    <div>
      <video id="video" src="C:\Users\10721\Desktop\label-img\label-img-vue\src\assets\8a62e3a554f840e0a1126c2e8de98210.mp4" controls style="width:300px;"></video>
      <img :src="imgSrc">
      <div>
        <el-button @click="screenShot">截图</el-button>
        <div id="photos"></div>
      </div>
      <canvas id="myCanvas" width="343" height="200"></canvas>
    </div>
</template>

<script>
export default {
  name:"Video",
  data() {
    return {
      imgSrc:'',
      listThumbnails: [],
    };
  },
  methods: {
    //截取当前帧的图片
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

      console.log(canvas.toDataURL())
      return canvas;
    }
  }
}
</script>

<style scoped>

</style>