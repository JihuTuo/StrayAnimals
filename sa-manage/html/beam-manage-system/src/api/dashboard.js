
import http from '@/util/http'


export default {
    // 登录
    getDashboardContent : params => {
        return http.get("/dashboard", params)
    }
}
