<template>
  <p>my photos</p>
  <div id="photo">
    <div class="card" v-for="message in images" :key="message.id">
      <img class="img" :src="message.url">
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "MyPhoto",
  data() {
    return {
      images: []
    }
  },
  mounted() {
    let self = this
    axios.post("/myImg", {fromID: this.$store.state.user.userID}).then(successResponse => {
      console.log(successResponse)
      self.images = successResponse.data
    }).catch(failResponse => {
      console.log(failResponse)
    })
  }
}
</script>

<style scoped>

</style>