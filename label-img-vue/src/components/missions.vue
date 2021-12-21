<template>
  <p>all missions</p>
  <div class="card" v-for="(mission) in missions" :key="mission.id">
    <router-link :to="{name:'missionDesc', params:{ID:mission.id}}">desc: {{ mission.description }}, state:
      {{ mission.state }}, fromID:
      {{ mission.fromID }} missionID: {{ mission.id }} toID: {{ mission.toID }}
    </router-link>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "missions",
  data() {
    return {
      missions: [],
    }
  },
  methods: {
    getHref(id) {
      return "/missionDesc-" + id
    }
  },
  mounted() {
    let self = this
    axios.post("missions").then((successResponse) => {
      self.missions = successResponse.data
      console.log(this.missions)
    }).catch((error) => {
      console.log(error)
    })
  }
}
</script>

<style scoped>

</style>