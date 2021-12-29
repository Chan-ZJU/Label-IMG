import {createApp} from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import JsonViewer from "vue3-json-viewer";
import VueCodeHighlight from 'vue-code-highlight'

const axios = require('axios');
axios.defaults.baseURL = 'http://localhost:8081/api'

createApp(App).use(store).use(router).use(ElementPlus).use(JsonViewer).use(VueCodeHighlight).mount("#app");
