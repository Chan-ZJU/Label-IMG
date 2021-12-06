<template>
    <div>
      <video src="http://chanzjuoss.oss-cn-hangzhou.aliyuncs.com/2021/12/06/28ab3c55-1640-4075-9236-6df473554e00.mp4" controls style="width:300px;"></video>
      <img :src="imgSrc">
      <div>
        <button @click="cutPicture">
          拍照
        </button>
      </div>
      <canvas id="myCanvas" width="343" height="200"></canvas>
    </div>
</template>

<script>
export default {
  name:"Video",
  data() {
    return {
      imgSrc:''
    };
  },
  methods: {
    //截取当前帧的图片
    cutPicture(){
      var v = document.querySelector("video");
      let canvas = document.getElementById('myCanvas')
      var ctx = canvas.getContext('2d');
      ctx.drawImage(v, 0, 0, 343, 200);
      var oGrayImg = canvas.toDataURL('image/jpeg');
      this.imgSrc = oGrayImg
    },
  }
}
</script>

<style scoped>

</style>