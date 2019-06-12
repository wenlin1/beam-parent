package com.hsshy.beam.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ColumnTitleMap {
    private Map<String, String> columnTitleMap = new HashMap<String, String>();
    private ArrayList<String> titleKeyList = new ArrayList<String> ();

    public ColumnTitleMap(String datatype) {
        switch (datatype) {
            case "customervisit":
                initClienteleInfoColu();
                initClienteleInfoTitleKeyList();
                break;
            default:
                break;
        }

    }
    /**
     * mysql用户表需要导出字段--显示名称对应集合
     */
    private void initClienteleInfoColu() {
        columnTitleMap.put("customerId", "店名编号");
        columnTitleMap.put("customerStoreName", "客户店名");
        columnTitleMap.put("visitTime", "签约时间");
        columnTitleMap.put("userName", "销售姓名");
        columnTitleMap.put("amount", "签约金额");
    }

    /**
     * mysql用户表需要导出字段集
     */
    private void initClienteleInfoTitleKeyList() {
        titleKeyList.add("customerId");
        titleKeyList.add("customerStoreName");
        titleKeyList.add("visitTime");
        titleKeyList.add("userName");
        titleKeyList.add("amount");
    }

    public Map<String, String> getColumnTitleMap() {
        return columnTitleMap;
    }

    public ArrayList<String> getTitleKeyList() {
        return titleKeyList;
    }


}
