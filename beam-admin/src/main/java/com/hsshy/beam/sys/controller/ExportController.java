package com.hsshy.beam.sys.controller;

import com.hsshy.beam.sys.service.IClienteleService;
import com.hsshy.beam.sys.service.impl.ColumnTitleMap;
import com.hsshy.beam.sys.service.impl.ExportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/exportdata")
public class ExportController {
    @Autowired
    private IClienteleService clienteleService;
    /**/
    @Autowired
    ExportDataService exportDataService;

    @GetMapping(value = "/excel")
    public void getEx( HttpServletResponse response, @RequestParam(required = true) String time_start, @RequestParam(required = true) String end_start) {
        try {
            Map<String,Object> params=new HashMap<>();
            List<Map<String,Object>> list = clienteleService.queryResultListMap(params);
            ArrayList<String> titleKeyList= new ColumnTitleMap("userinfo").getTitleKeyList();
            Map<String, String> titleMap = new ColumnTitleMap("userinfo").getColumnTitleMap();
            exportDataService.exportDataToEx(response, titleKeyList, titleMap, list);
        } catch (Exception e) {
            //
            System.out.println(e.toString());
        }
    }

}
