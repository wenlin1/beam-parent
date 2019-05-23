import http from '@/util/http'

export default {
    // 登录
    getData : params => {
        return http.get("/sys/myclient/page/list", params);
    },
    // 保存
    save : params => {
        return http.post("/sys/myclient/save", params)
    },
    // 删除
    batchDelete : ids => {
        return http.post("/sys/myclient/delete", ids)
    },
}
