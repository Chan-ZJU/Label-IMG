<template>
  欢迎用户: {{ this.$store.state.user.username }}
  <br>
  当前站点总任务数: {{ this.count[0] }}
  <br>
  当前站点可领取任务数: {{ this.count[1] }}
  <br>
  当前站点已完成任务数: {{ this.count[2] }}
  <br>
  当前站点审核完毕任务数: {{ this.count[3] }}
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