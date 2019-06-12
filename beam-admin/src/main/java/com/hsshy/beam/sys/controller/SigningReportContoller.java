package com.hsshy.beam.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hsshy.beam.common.base.controller.BaseController;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.sys.entity.CustomerVisit;
import com.hsshy.beam.sys.service.ICustomerVisitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "signingReportContoller", tags = {"客户拜访信息接口"})
@RequestMapping("/signingReport")
@RestController
public class SigningReportContoller extends BaseController {
    @Autowired
    private ICustomerVisitService customerVisitService;

    @RequiresPermissions("signingReport:list")
    @ApiOperation(value = "分页列表")
    @GetMapping(value = "/page/list")
    public Object pageList(CustomerVisit customerVisit) {
        customerVisit.setVisitType(4);
        IPage<CustomerVisit> list = customerVisitService.selectPageList(customerVisit);
        return R.ok(customerVisitService.selectPageList(customerVisit));
    }

}
