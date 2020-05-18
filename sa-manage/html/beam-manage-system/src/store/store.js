import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        menuActiveIndex: 'index'
    },
    mutations: {
        changeMenuActiveIndex (state, index) {
            state.menuActiveIndex = index;
        },
    }
})
