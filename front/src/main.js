// import Vue from 'vue'
import { createApp } from 'vue'
import App from './App.vue'
import "./assets/scss/common.scss"
import router from './router'
import axios from 'axios';

const app = createApp(App)

// app.config.globalProperties.$toggle = true; 

app.use(router)

app.mount('#app')

app.config.globalProperties.$axios = axios;

