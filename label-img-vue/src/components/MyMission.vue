<template>
  <p>我创建的任务</p>
  <div class="card" v-for="(mission) in missions" :key="mission.id">
    <p>description: {{ mission.description }}, state: {{ mission.state }}</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "MyMission",
  data() {
    return {
      missions: [],
    }
  },
  methods: {},
  mounted() {
    let self = this
    let form = new FormData()
    form.append("userID", this.$store.state.user.userID)
    axios.post("myMission", form).then((successResponse) => {
      self.missions = successResponse.data
      console.log(this.missions)
    }).catch((error) => {
      console.log(error)
    })
  },
}
</script>

<style scoped>

</style>