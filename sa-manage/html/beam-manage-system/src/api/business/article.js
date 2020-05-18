
import http from '@/util/http'


export default {
    // 登录
    getData : params => {
        return http.get("/blog/article/page/list", params);
    },
    // 保存
    save : params => {
        return http.post("/blog/article/save", params)
    },
    // 保存
    saveContent : params => {
        return http.post("/blog/article/save/content", params)
    },
    // 批量删除
    batchDelete : ids => {
        return http.post("/blog/article/delete", ids)
    },
    getArticleList : params => {
        return http.get("/blog/article/list", params);
    },
    getArticleInfo:articleId=>{
        return http.get("/blog/article/info", {articleId:articleId});
    },
    getCategoryList : params => {
        return http.get("/blog/articlecategory/list", params);
    },
    uploadImg : params => {
        return http.upload("/file/upload", params);
    },


}
