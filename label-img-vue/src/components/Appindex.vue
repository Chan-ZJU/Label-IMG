<template>
<div>
  <br>
  userID: {{this.$store.state.user.userID}}
  <br>
  <Upload></Upload>
  <Video></Video>
</div>
</template>

<script>
import Upload from "@/components/Upload";
import Video from "@/components/Video";
export default {
  name: "Appindex",
  components: {Video, Upload},
}
</script>

<style scoped>

</style>