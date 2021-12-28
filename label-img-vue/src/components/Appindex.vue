<template>
  <br>
  欢迎用户:
  <el-tag>{{ this.$store.state.user.username }}</el-tag>
  <el-divider></el-divider>
  当前站点总任务数:
  <el-tag type="success">{{ this.count[0] }}</el-tag>
  <div class="progress">
    <el-progress type="dashboard" stroke-width="10"
                 :percentage="(100*parseFloat( this.count[1])/parseFloat(this.count[0])).toFixed(1)">
      <template #default="{ percentage }">
        <span class="percentage-value">{{ percentage }}%</span>
        <span class="percentage-label">可领取任务</span>
      </template>
    </el-progress>
    <br>
    <el-progress type="dashboard" stroke-width="10"
                 :percentage="(100*parseFloat( this.count[2])/parseFloat(this.count[0])).toFixed(1)">
      <template #default="{ percentage }">
        <span class="percentage-value">{{ percentage }}%</span>
        <span class="percentage-label">已领取任务</span>
      </template>
    </el-progress>
    <el-progress type="dashboard" stroke-width="10"
                 :percentage="(100*parseFloat( this.count[3])/parseFloat(this.count[0])).toFixed(1)">
      <template #default="{ percentage }">
        <span class="percentage-value">{{ percentage }}%</span>
        <span class="percentage-label">审核完毕任务</span>
      </template>
    </el-progress>
  </div>
</template>
<script>
import axios from "axios";

export default {
  name: "Appindex",
  data() {
    return {
      count: []
    }
  },
  mounted() {
    axios.post("getCount").then(
        (success) => this.count = success.data
    ).catch((e) => console.log(e))
  }
}
</script>

<style scoped>
.progress {
  width: auto;
  margin: auto;
  padding: 20px;
}

.percentage-label {
  display: block;
  margin-top: 10px;
  font-size: 12px;
}

.percentage-value {
  display: block;
  margin-top: 10px;
  font-size: 28px;
}
</style>