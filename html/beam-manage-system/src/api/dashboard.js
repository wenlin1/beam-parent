
import http from '@/util/http'


export default {
    // 登录
    getDashboardContent : params => {
        return http.get("/dashboard", params)
    },
    getData : params => {
        return http.get("/dashboard/page/list", params);
    },
    //总客户数
    getCustomerTotal:params => {
        return http.get("/clientele/getCustomerTotal", params)
    }
}
