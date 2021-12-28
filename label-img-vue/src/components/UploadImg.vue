<template>
  <p>上传图片</p>
  <el-upload
      class="upload"
      ref="upload"
      :data="user"
      action="http://localhost:8081/api/ossUpload"
      multiple
      list-type="picture-card"
      show-file-list
      :on-preview="handlePreview"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :on-success="handleSuccess"
      :file-list="fileList"
  >
    <el-icon class="el-icon--upload">
      <upload-filled/>
    </el-icon>
    <div class="el-upload__text">
      <em>click to upload</em>
    </div>
    <template #tip>
      <div class="el-upload__tip">
        请上传 jpg/png 图片
      </div>
    </template>
  </el-upload>
  <el-dialog v-model="dialogVisible">
    <el-image :src="dialogImageUrl" alt=""/>
  </el-dialog>
  <br>
</template>

<script setup>
import {UploadFilled} from '@element-plus/icons'
</script>

<script>
export default {
  name: "Upload",
  data() {
    return {
      fileList: [],
      url: '',
      // type: 0 is image
      user: {ID: this.$store.state.user.userID, type: 0},
      dialogImageUrl: '',
      dialogVisible: false
    }
  },
  methods: {
    beforeUpload(file) {
      let isJPG = file.type === 'image/jpeg'
      let isPNG = file.type === 'image/png'
      if (!isJPG && !isPNG) {
        this.$message.error("请上传图片")
      }
      return isJPG || isPNG
    },
    handlePreview(file) {
      this.dialogImageUrl = file.response
      this.dialogVisible = true
    },
    handleRemove(file) {
      console.log(file)
    },
    handleSuccess(response) {
      console.log(response)
      this.url = response
      // this.$emit('onUpload')
    },
  }
}
</script>

<style scoped>

</style>