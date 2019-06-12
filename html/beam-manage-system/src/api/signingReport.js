import http from '@/util/http'


export default {
    // 登录
    getData: params => {
        return http.get("/signingReport/page/list", params);
    },
    export: params => {
        return http.get("/exportdata/excel", params);
    }
}
