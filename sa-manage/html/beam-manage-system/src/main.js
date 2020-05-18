import Vue from 'vue';
import App from './App';
import router from './router';
import store from './store/store';
import axios from 'axios';
import ElementUI from 'element-ui';
import AMap from 'vue-amap'
import '../static/css/theme-ff406d/index.css' //粉红色主题
// import 'element-ui/lib/theme-chalk/index.css';    // 默认主题
import 'element-ui/lib/theme-chalk/icon.css';    // 默认icon
import "babel-polyfill";
import utils from '@/util/utils'


Vue.prototype.$utils = utils;
Vue.use(ElementUI, { size: 'small' });
Vue.prototype.$axios = axios;
Vue.prototype.getPerms = function (){//changeData是函数名
    let buttonItems = localStorage.getItem("buttonItems");
    if(buttonItems){
        return buttonItems;
    }
    else {
        return [];
    }
}
//使用钩子函数对路由进行权限跳转
router.beforeEach((to, from, next) => {
    let buttonItems = localStorage.getItem("buttonItems");
    if (to.meta.title) {
        document.title = to.meta.title;
    }
    if(to.meta.permission&&buttonItems){
        if(buttonItems.indexOf(to.meta.perms)!=-1){
           next();
        }
        else {
           next('/403');
        }
    }else{
        // 简单的判断IE10及以下不进入富文本编辑器，该组件不兼容
        if(navigator.userAgent.indexOf('MSIE') > -1 && to.path === '/editor'){
            Vue.prototype.$alert('vue-quill-editor组件不兼容IE10及以下浏览器，请使用更高版本的浏览器查看', '浏览器不兼容通知', {
                confirmButtonText: '确定'
            });
        }else{
            next();
        }
    }
})

Vue.use(AMap);
// 初始化地图
AMap.initAMapApiLoader({
    // 高德的key
    key: '818eeffae9ec8deb70b60d2794ae3906',
    // 插件集合
    plugin: ['AMap.Autocomplete', 'AMap.PlaceSearch', 'AMap.Scale', 'AMap.OverView', 'AMap.ToolBar', 'AMap.MapType', 'AMap.PolyEditor', 'AMap.CircleEditor','AMap.Geocoder','AMap.Geolocation'],
    v: '1.4.4'
});
new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app');
