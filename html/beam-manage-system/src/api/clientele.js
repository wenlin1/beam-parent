
import http from '@/util/http'


export default {
    // 登录
    getData : params => {
        return http.get("/sys/clientele/page/list", params);
    },
    // 保存
    save : params => {
        return http.post("/sys/clientele/save", params)
    },
    // 批量删除
    batchDelete : ids => {
        return http.post("/sys/clientele/delete", ids)
    },
}
