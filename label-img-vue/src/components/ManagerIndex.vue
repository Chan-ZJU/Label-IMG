<template>
  管理员主页
  <div class="card" v-for="(mission) in missions" :key="mission.id">
    <router-link :to="{name:'checkMission', params:{ID:mission.id}}">desc: {{ mission.description }}, state:
      {{ mission.state }}, fromID:
      {{ mission.fromID }} missionID: {{ mission.id }} toID: {{ mission.toID }}
    </router-link>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ManagerIndex",
  data() {
    return {
      missions: [],
    }
  },
  mounted() {
    axios.post("getCheck").then((success) => {
      this.missions = success.data
    }).catch((e) => {
      console.log(e)
    })
  }
}
</script>

<style scoped>

</style>