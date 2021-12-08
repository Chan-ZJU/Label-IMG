<template>
  <div>
    <br>
    userID: {{ this.$store.state.user.userID }}
    <br>
  </div>
  <Video></Video>
</template>

<script>
import Video from "@/components/Video";
export default {
  name: "Appindex",
  components: {Video}
}
</script>

<style scoped>

</style>