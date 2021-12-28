<template>
  <div id="photo">
    <el-divider></el-divider>
    <el-button @click="(drawer = true);getCheckedBox()">创建任务</el-button>
    <el-divider></el-divider>
    <el-backtop :bottom="100">
      <div
          style=" height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0, 0, 0, 0.12);
        text-align: center;
        line-height: 40px;
        color: #1989fa; "
      >
        TOP
      </div>
    </el-backtop>
    <el-drawer v-model="drawer" title="创建任务" :before-close="handleClose">
      <el-input v-model="missionDesc" placeholder="请输入标注内容"></el-input>
      <el-button @click="createMission();drawer=false" :disabled="cannotCreate">创建</el-button>
    </el-drawer>
    <el-row gutter="20" class="row_col">
      <div class="card" v-for="(message) in images" :key="message.id">
        <el-col>
          <label v-bind:for="message.id.toString()">
            <el-image :fit="fit" :src="message.url" style="width: 280px;height: 210px"></el-image>
          </label>
          <br>
          <input type="checkbox" name="chooseResult" v-bind:id="message.id.toString()">
        </el-col>
      </div>
      <el-divider></el-divider>
    </el-row>
  </div>
</template>

<script>
import axios from "axios";
import {ElMessageBox} from "element-plus";

export default {
  name: "MyPhoto",
  data() {
    return {
      images: [],
      choose: [],
      drawer: false,
      missionDesc: '',
      cannotCreate: false,
      fit: 'contain',
    }
  },
  methods: {
    handleClose(done) {
      ElMessageBox.confirm('确认关闭?').then(() => {
        done()
      }).catch(() => {
      })
    },
    getCheckedBox() {
      let obj = document.getElementsByName("chooseResult")
      let num = 0
      for (let k in obj) {
        if (obj[k].checked) {
          this.choose[obj[k].id] = true
          num = num + 1
        }
      }
      this.cannotCreate = num === 0;
    },
    createMission() {
      console.log(this.choose)
      console.log(this.missionDesc)
      let form = new FormData()
      form.append("missionDesc", this.missionDesc)
      form.append("choose", this.choose)
      form.append("fromID", this.$store.state.user.userID)
      console.log(form)

      axios.post("/createMission", form).then((successResponse) => {
        console.log(successResponse)
      }).catch((error) => {
        console.log(error)
      })
      this.$message.success("创建成功")
    }
  },
  mounted() {
    let self = this
    axios.post("/myImg", {fromID: this.$store.state.user.userID}).then(successResponse => {
      self.images = successResponse.data
      console.log(this.images)
    }).catch(failResponse => {
      console.log(failResponse)
    })
  }
}
</script>

<style scoped>
.row_col {
  margin: auto;
  padding: 30px;
}
</style>