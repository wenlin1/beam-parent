import http from '@/util/http'

export default {
    // 登录
    getData : params => {
        return http.get("/sys/myclient/page/list", params);
    },
    // 登录
    getMyData : params => {
        return http.get("/sys/myclient/page/mylist", params);
    },
    // 保存
    save : params => {
        return http.post("/sys/myclient/save", params)
    },
    //添加我的客户
    addMyClient : addIds => {
        return http.post("/sys/myclient/addMyClient", addIds)
    },
    // 删除
    deleteMyClient : ids => {
        return http.post("/sys/myclient/deleteMyClient", ids)
    },
}
