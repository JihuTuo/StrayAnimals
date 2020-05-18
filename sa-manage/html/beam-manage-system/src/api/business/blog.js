
import http from '@/util/http'


export default {

    getArticleList : params => {
        return http.get("/api/blog/list", params);
    },
    getArticleInfo:shortCode => {
        return http.get("/api/blog/detail/"+shortCode);
    },
    addReadNum:shortCode => {
        return http.get("/api/blog/add/read/num",{shortCode:shortCode});
    },
    getCategoryList : params => {
        return http.get("/api/blog/category/list", params);
    },

}
