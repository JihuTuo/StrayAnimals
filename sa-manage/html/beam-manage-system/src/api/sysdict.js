import http from '@/util/http'


export default {
    getData: params => {
        return http.get("/sys/dict/page/list", params);
    },
    save: params => {
        return http.post("/sys/dict/save", params)
    },

    batchDelete: ids => {
        return http.post("/sys/dict/delete", ids)
    },
    getDictList: params => {
        return http.get("/sys/dict/list", params);
    },


}
