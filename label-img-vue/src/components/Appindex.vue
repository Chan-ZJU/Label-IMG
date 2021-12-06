<template>
<div>
  Hello world!
  <br>
  {{this.$store.state.user}}
  <br>
  <Upload></Upload>
</div>
</template>

<script>
import Upload from "@/components/Upload";
export default {
  name: "Appindex",
  components: {Upload},
}
</script>

<style scoped>

</style>