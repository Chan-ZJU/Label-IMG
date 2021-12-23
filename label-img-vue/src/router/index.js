import {createRouter, createWebHashHistory} from 'vue-router'
import Appindex from "@/components/Appindex";
import Login from "@/components/Login";
import store from "@/store";
import Home from "@/components/Home";
import Signup from "@/components/Signup";
import UploadImg from "@/components/UploadImg";
import UploadVideo from "@/components/UploadVideo";
import MyPhoto from "@/components/MyPhoto";
import MyMission from "@/components/MyMission";
import missions from "@/components/missions";
import claimedMission from "@/components/claimedMission";
import MissionDesc from "@/components/MissionDesc";
import PureMissionDesc from "@/components/PureMissionDesc";
import doMissionWithPureCanvas from "@/components/doMissionWithPureCanvas";

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
            },
            {
                path: '/uploadIMG',
                name: 'UploadImg',
                component: UploadImg,
                meta: {
                    requireAuth: true
                }
            },
            {
                path: 'myPhoto',
                name: 'myphoto',
                component: MyPhoto,
                meta: {
                    requireAuth: true
                }
            },
            {
                path: 'uploadVIDEO',
                name: 'UploadVideo',
                component: UploadVideo,
                meta: {
                    requireAuth: true
                }
            },
            {
                path: 'myMission',
                name: 'mymission',
                component: MyMission,
                meta: {
                    requireAuth: true
                }
            },
            {
                path: 'claimedMission',
                name: 'claimedMissions',
                component: claimedMission,
                meta: {
                    requireAuth: true
                }
            },
            {
                path: 'missions',
                name: 'missions',
                component: missions,
                meta: {
                    requireAuth: true
                }
            },
            {
                path: '/missionDesc/:ID',
                name: 'missionDesc',
                component: MissionDesc,
                meta: {
                    requireAuth: true
                }
            },
            {
                path: '/pureDesc/:ID',
                name: 'pureDesc',
                component: PureMissionDesc,
                meta: {
                    requireAuth: true
                }
            },
            {
                path: '/doMission/:ID',
                name: 'doMission',
                component: doMissionWithPureCanvas,
                meta: {
                    requireAuth: true
                }
            },
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
