import http from '@/util/http'
export default {
    // 登录
    login : params => {
        return http.post("/login", params)
    },
    getCaptchaSrc : () => {
        let time = new Date().getTime();
        return http.getBaseUrl()+"/captcha/pic.jpg?time="+time;
    },
    getCaptchaOpen : () => {
        return http.get("/captcha/open");
    }
}
