
import http from '@/util/http'


export default {
    getData : params => {
        return http.get("/blog/articlecategory/page/list", params);
    },
    // 保存
    save : params => {
        return http.post("/blog/articlecategory/save", params)
    },
    // 批量删除
    batchDelete : ids => {
        return http.post("/blog/articlecategory/delete", ids)
    },
    changeStatus : (id, flag) => {
        return http.post("/blog/articlecategory/change/status/" + flag, id)
    },


}
