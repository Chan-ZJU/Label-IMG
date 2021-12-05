<template>
  <el-upload
      class="upload"
      ref="upload"
      drag
      action="http://localhost:8081/api/upload"
      multiple
      show-file-list
      :on-preview="handlePreview"
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
    handlePreview(file) {
      console.log(file)
    },
    handleRemove(file) {
      console.log(file)
    },
    beforeRemove(file) {
      return this.confirm(`确认删除${file.name}?`)
    },
    handleSuccess(response) {
      this.url = response
      // this.$emit('onUpload')
      this.message.warning('上传成功')
    }
  }
}
</script>

<style scoped>

</style>