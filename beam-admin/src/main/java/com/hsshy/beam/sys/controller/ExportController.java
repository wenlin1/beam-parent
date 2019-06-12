package com.hsshy.beam.sys.controller;

import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.sys.entity.CustomerVisit;
import com.hsshy.beam.sys.service.ICustomerVisitService;
import com.hsshy.beam.sys.service.impl.ColumnTitleMap;
import com.hsshy.beam.sys.service.impl.ExportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/exportdata")
public class ExportController {
    @Autowired
    private ICustomerVisitService customerVisitService;
    @Autowired
    ExportDataService exportDataService;

    @GetMapping(value = "/excel")
    public void getEx(HttpServletResponse response, String startTime, String endTime) {
        try {
            Map<String, Object> params = new HashMap<>();
            if (startTime != null && endTime != null) {
                params.put("startTime", startTime);
                params.put("endTime", endTime);
            }
            List<Map<String, Object>> list = customerVisitService.queryResultListMap(params);
            ArrayList<String> titleKeyList = new ColumnTitleMap("customervisit").getTitleKeyList();
            Map<String, String> titleMap = new ColumnTitleMap("customervisit").getColumnTitleMap();
            exportDataService.exportDataToEx(response, titleKeyList, titleMap, list, startTime);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

}
