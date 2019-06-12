import http from '@/util/http'


export default {
    // 登录
    getData: params => {
        return http.get("/clientele/page/list", params);
    },
    // 保存
    save: params => {
        return http.post("/clientele/save", params)
    },
    // 批量删除
    batchDelete: ids => {
        return http.post("/clientele/delete", ids)
    },

    getUser: params => {
        return http.get("/sys/user/userList", params);

    },
}
