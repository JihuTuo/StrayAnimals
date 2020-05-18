import axios from 'axios'
import { commonParams } from '../api/config'
import { Message } from 'element-ui'

const Http = {
    getBaseUrl(){
        return "/beam_ht";
    },
    get(url, params) {
        const data = Object.assign({}, commonParams, params);
        url = this.getBaseUrl() + url
        return axios.get(url, {
            params: data,
        }).then((res) => {
            if(res.data.error===false){
                return Promise.resolve(res.data); //成功
            }
            else{
                if (res.data.code === 401) {
                    Message.error(res.data.msg);
                    window.location = "/#/login";
                    return Promise.reject(res.data);
                }
                else if(res.data.code === 403){
                    Message.error(res.data.msg);
                    window.location = "/#/403";
                    return Promise.resolve(res.data)
                }
                else{
                    return Promise.resolve(res.data)
                }

            }
        }).catch((err) => {
            // console.log(err.request)
            //超时之后在这里捕抓错误信息.
            if (err.response) {

                var res = {
                    code: err.code,
                    msg: err.message,
                }
                return Promise.reject(res)

            } else if (err.request) {

                if(err.request.readyState == 4 && err.request.status == 0){

                    //我在这里重新请求
                    var res = {
                        code: 403,
                        msg: "网络链接错误，请刷新重试！",
                    }

                    this.$message.error("网络链接错误，请刷新重试！")
                    return Promise.reject(res)
                }
            } else {

                Message.error( err.message)
                return Promise.reject(res)
            }
            return Promise.reject(err)

        })
    },
    post(url, data) {
        axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
        url = this.getBaseUrl() + url
        return axios.post(url, data).then((res) => {
            if(res.data.error===false){
                return Promise.resolve(res.data); //成功

            }
            else{
                if (res.data.code === 401) {
                    Message.error(res.data.msg);
                    window.location = "/#/login";
                    return Promise.resolve(res.data) //拒绝
                }
                else if(res.data.code === 403){
                    Message.error(res.data.msg);
                    window.location = "/#/403";
                    return Promise.resolve(res.data) //拒绝
                }
                else{
                    return Promise.resolve(res.data) //拒绝
                }


            }

        }).catch((err) => {
            //超时之后在这里捕抓错误信息.

            if (err.response) {
                var res = {
                    code: err.code,
                    msg: err.message,
                }
                return Promise.reject(res)
            } else if (err.request) {
                if(err.request.readyState == 4 && err.request.status == 0){
                    //我在这里重新请求
                    var res = {
                        code: 403,
                        msg: "网络链接错误，请刷新重试！",
                    }

                    this.$message.error("网络链接错误，请刷新重试！")
                    return Promise.reject(res)
                }
            } else {

                Message.error( err.message)
                return Promise.reject(res)
            }
            return Promise.reject(err)
        })
    },
    upload(url, data) {
        axios.defaults.headers.post['Content-Type'] = 'multipart/form-data';
        url = "/beam_ht" + url
        return axios.post(url, data).then((res) => {
            if(res.data.error===false){
                return Promise.resolve(res.data); //成功
            }
            else{
                if (res.data.code === -1) {
                    Message.error(res.data.msg);
                    window.location = "/#/login";
                    return Promise.resolve(res.data) //拒绝
                }
                else if(res.data.code === 403){
                    Message.error(res.data.msg);
                    window.location = "/#/403";
                    return Promise.resolve(res.data) //拒绝
                }
                else{
                    return Promise.resolve(res.data) //拒绝
                }
            }

        }).catch((err) => {
            //超时之后在这里捕抓错误信息.

            if (err.response) {
                var res = {
                    code: err.code,
                    msg: err.message,
                }
                return Promise.reject(res)
            } else if (err.request) {
                if(err.request.readyState == 4 && err.request.status == 0){
                    //我在这里重新请求
                    var res = {
                        code: 403,
                        msg: "网络链接错误，请刷新重试！",
                    }
                    this.$message.error("网络链接错误，请刷新重试！")
                    return Promise.reject(res)
                }
            } else {

                Message.error( err.message)
                return Promise.reject(res)
            }
            return Promise.reject(err)
        })
    },

};

export default Http;
