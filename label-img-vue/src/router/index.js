import {createRouter, createWebHashHistory} from 'vue-router'
import Appindex from "@/components/Appindex";
import Login from "@/components/Login";
import store from "@/store";
import Home from "@/components/Home";
import Signup from "@/components/Signup";

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        redirect: '/index',
        children: [
            {
                path: '/index',
                name: 'AppIndex',
                component: Appindex,
                meta: {
                    requireAuth: true
                }
            }
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/signup',
        name: 'Signup',
        component: Signup
    },
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    if (to.meta.requireAuth) {
        if (store.state.user.username) {
            next()
        } else {
            next({
                path: 'login',
                query: {redirect: to.fullPath}
            })
        }
    } else {
        next()
    }
})


export default router