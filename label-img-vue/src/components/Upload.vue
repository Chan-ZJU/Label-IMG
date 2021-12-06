<template>
  <el-upload
      class="upload"
      ref="upload"
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
  <el-image src="http://localhost:8081/BS2021/12/05f69542bd4a674c60846ee22e24c50d2e.jpg"></el-image>
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
      url: ''
    }
  },
  methods: {
    beforeUpload(file){
      console.log(file.raw)
    },
    handlePreview(file) {
      console.log(file)
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