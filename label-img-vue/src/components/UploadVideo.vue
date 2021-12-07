<template>
  <el-upload
      class="upload"
      ref="upload"
      :data="user"
      drag
      action="http://localhost:8081/api/ossUpload"
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
    <video width=500 :src="dialogVideoUrl" controls alt=""></video>
  </el-dialog>
  <br>
  <Video></Video>
</template>

<script setup>
import {UploadFilled} from '@element-plus/icons'
import Video from "@/components/Video";
</script>

<script>
export default {
  name: "UploadVideo",
  data() {
    return {
      fileList: [],
      url: '',
      //type: 1 is video
      user: {ID: this.$store.state.user.userID, type:1},
      dialogVisible: false,
      dialogVideoUrl: ''
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
    }
  }
}
</script>

<style scoped>

</style>