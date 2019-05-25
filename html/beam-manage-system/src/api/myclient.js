import http from '@/util/http'

export default {
    // 登录
    getData : params => {
        return http.get("/myclient/page/list", params);
    },
    // 登录
    getMyData : params => {
        return http.get("/myclient/page/mylist", params);
    },
    // 保存
    save : params => {
        return http.post("/myclient/save", params)
    },
    //添加我的客户
    addMyClient : addIds => {
        return http.post("/myclient/addMyClient", addIds)
    },
    // 删除
    deleteMyClient : ids => {
        return http.post("/myclient/deleteMyClient", ids)
    },

}
