
import http from '@/util/http'


export default {
    // 登录
    getData : params => {
        return http.get("/sys/role/page/list", params);
    },
    // 保存
    save : params => {
        return http.post("/sys/role/save", params)
    },

    // 批量删除
    batchDelete : ids => {
        return http.post("/sys/role/delete", ids)
    },
    getMenuTreeData : params => {
        return http.get("/sys/menu/tree/menu", params);
    },
    getCheckMenuData:params => {
        return http.get("/sys/role/menu/list", params);
    },
    saveMuenPerms:params => {
        console.log(params);
        return http.post("/sys/role/save/menu/perm", params);
    }


}
