<template>
  <p>all missions</p>
  <div class="card" v-for="(mission) in missions" :key="mission.id">
    <p>desc: {{ mission.description }}, state: {{ mission.state }}, fromID: {{ mission.fromID }}</p>
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
  methods: {},
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