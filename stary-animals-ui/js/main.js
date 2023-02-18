import axios from 'axios'
import vue from 'vue'


axios.interceptors.request.use(
    config => {
        let token = localStorage.getItem("x-auth-token");
        if (token) {  // 判断是否存在token，如果存在的话，则每个http header都加上token
            config.headers.token = `${token}`;
        }
        if (config.url.indexOf(url) === -1) {
            config.url = url + config.url;/*拼接完整请求路径*/
        }
        return config;
    },
    err => {
        return Promise.reject(err);
    });
