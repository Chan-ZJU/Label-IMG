<template>
  <el-menu
      :default-active="activeIndex2"
      router
      class="el-menu-demo"
      mode="horizontal"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
      @select="handleSelect"
  >
    <el-menu-item index="/index">首页</el-menu-item>
    <el-sub-menu index="2">
      <template #title>上传资源</template>
      <el-menu-item index="/uploadIMG">上传图片</el-menu-item>
      <el-menu-item index="/uploadVIDEO">上传视频</el-menu-item>
    </el-sub-menu>
    <el-menu-item index="/myPhoto">我的图库</el-menu-item>
    <el-menu-item index="/missions">任务中心</el-menu-item>
    <el-sub-menu index="3">
      <template #title>我的任务</template>
      <el-menu-item index="/myMission">我创建的任务</el-menu-item>
      <el-menu-item index="/claimedMission">我认领的任务</el-menu-item>
    </el-sub-menu>
    <el-menu-item index="/login" style="color: antiquewhite ;right: 5%;position: absolute">当前用户:
      {{ this.$store.state.user.username }}
    </el-menu-item>
  </el-menu>
</template>

<script>
import {ref} from "vue";

export default {
  name: "NavMenu",
  setup() {
    const activeIndex = ref('1')
    const activeIndex2 = ref('1')
    const handleSelect = (key, keyPath) => {
      console.log(key, keyPath)
    }
    return {
      activeIndex,
      activeIndex2,
      handleSelect,
    }
  }
}
</script>

<style scoped>

</style>