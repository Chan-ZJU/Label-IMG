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
      :before-remove="beforeRemove"
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
        jpg/png files with a size less than 500kb
      </div>
    </template>
  </el-upload>
  <el-dialog v-model="dialogVisible">
    <img width=500 :src="dialogImageUrl" alt=""/>
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
      console.log(file.raw)
    },
    handlePreview(file) {
      this.dialogImageUrl = file.response
      this.dialogVisible = true
    },
    handleRemove(file) {
      console.log(file)
    },
    beforeRemove(file) {
      //TODO: add axios to delete file
      this.$confirm({content: `确认删除 ${file.name}?`, title: "确认删除图片?"})
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