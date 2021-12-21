<template>
  <p>我认领的任务</p>
  <div class="card" v-for="(mission) in missions" :key="mission.id">
    <router-link :to="{name:'doMission', params:{ID:mission.id}}">desc: {{ mission.description }}, state:
      {{ mission.state }}, fromID:
      {{ mission.fromID }}, missionID: {{ mission.id }}, toID: {{ mission.toID }}
    </router-link>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "claimedMission",
  data() {
    return {
      missions: [],
    }
  },
  mounted() {
    axios.post("myClaimedMission", {fromID: this.$store.state.user.userID}).then((success) => {
      this.missions = success.data
      console.log(this.missions)
    }).catch((error) => {
      console.log(error)
    })
  }
}
</script>

<style scoped>

</style>