import http from '@/util/http'


export default {
    // 登录
    getData: params => {
        return http.get("/customerVisit/page/list", params);
    },
    // 保存
    save: params => {
        return http.post("/customerVisit/save", params)
    },
    // 删除
    batchDelete: ids => {
        return http.post("/customerVisit/delete", ids)
    },
    getClinetlist: params => {
        return http.get("/myclient/getClinetlist", params)
    },
    info: params => {
        return http.get("/customerVisit/info", params);
    },
    export: params => {
        return http.get("/exportdata/excel", params);
    }
}
