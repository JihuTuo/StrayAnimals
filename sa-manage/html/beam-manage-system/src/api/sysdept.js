
import http from '@/util/http'


export default {

    getData : params => {
        return http.get("/sys/dept/page/list", params);
    },
    getTreeData : params => {
        return http.get("/sys/dept/tree/dept", params);
    },
    // 保存
    save : params => {
        return http.post("/sys/dept/save", params)
    },
    // 详情
    info : params => {
        return http.get("/sys/dept/info", params)
    },

    // 批量删除
    batchDelete : ids => {
        return http.post("/sys/dept/delete", ids)
    },


}
