
import http from '@/util/http'
export default {
    // 登录
    getData : params => {
        return http.get("/sys/schedule/page/list", params);
    },
    // 保存
    save : params => {
        return http.post("/sys/schedule/save", params)
    },
    // 保存
    update : params => {
        return http.post("/sys/schedule/update", params)
    },

    // 批量删除
    batchDelete : ids => {
        return http.post("/sys/schedule/delete", ids)
    },

    // 批量删除
    runJob : ids => {
        return http.post("/sys/schedule/run", ids)
    },
    // 批量删除
    pauseJob : ids => {
        return http.post("/sys/schedule/pause", ids)
    },
    // 批量删除
    resumeJob : ids => {
        return http.post("/sys/schedule/resume", ids)
    },


}
