import {createApp} from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import VueSelectImage from 'vue-select-image'
require('vue-select-image/dist/vue-select-image.css')

const axios = require('axios');
axios.defaults.baseURL = 'http://localhost:8081/api'

createApp(App).use(store).use(router).use(ElementPlus).use(VueSelectImage).mount("#app");
