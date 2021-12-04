import { createRouter, createWebHashHistory } from 'vue-router'
import Home from '../views/Home.vue'

import Appindex from "@/components/home/Appindex";
import Login from "@/components/Login";

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/index',
    name: 'AppIndex',
    component: Appindex
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
