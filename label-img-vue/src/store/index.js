import {createStore} from 'vuex'

export default createStore({
    state: {
        user: {
            username: window.sessionStorage.getItem('user' || '[]') == null ? '' : JSON.parse(window.sessionStorage.getItem('user' || '[]')).username
        }
    },
    mutations: {
        login(state, user) {
            state.user = user
            window.sessionStorage.setItem('user', JSON.stringify(user))
        }
    },
    actions: {},
    modules: {}
})
