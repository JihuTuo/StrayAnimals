
import http from '@/util/http'


export default {

    getData : params => {
        return http.get("/sys/menu/page/list", params);
    },
    getTreeData : params => {
        return http.get("/sys/menu/tree/menu", params);
    },
    // 保存
    save : params => {
        return http.post("/sys/menu/save", params)
    },
    // 详情
    info : params => {
        return http.get("/sys/menu/info", params)
    },

    // 批量删除
    batchDelete : ids => {
        return http.post("/sys/menu/delete", ids)
    },



}
