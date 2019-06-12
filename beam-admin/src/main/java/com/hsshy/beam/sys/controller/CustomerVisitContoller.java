package com.hsshy.beam.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hsshy.beam.common.base.controller.BaseController;
import com.hsshy.beam.common.factory.impl.ConstantFactory;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.common.utils.ToolUtil;
import com.hsshy.beam.sys.entity.CustomerVisit;
import com.hsshy.beam.sys.entity.Dept;
import com.hsshy.beam.sys.entity.User;
import com.hsshy.beam.sys.service.IClienteleService;
import com.hsshy.beam.sys.service.ICustomerVisitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Api(value = "CustomerVisitContoller", tags = {"客户拜访信息接口"})
@RequestMapping("/customerVisit")
@RestController
public class CustomerVisitContoller extends BaseController {
    @Autowired
    private ICustomerVisitService customerVisitService;
    @Autowired
    private IClienteleService clienteleService;

    @RequiresPermissions("customerVisit:list")
    @ApiOperation(value = "分页列表")
    @GetMapping(value = "/page/list")
    public Object pageList(CustomerVisit customerVisit) {
        IPage<CustomerVisit> list = customerVisitService.selectPageList(customerVisit);
        return R.ok(customerVisitService.selectPageList(customerVisit));
    }

    @ApiOperation(value = "查看分页列表")
    @GetMapping(value = "/page/viewlist")
    public Object pageViewList(CustomerVisit customerVisit) {
        IPage<CustomerVisit> list = customerVisitService.selectPageList(customerVisit);
        return R.ok(customerVisitService.selectPageList(customerVisit));
    }

    @ApiOperation("保存用户")
    @PostMapping(value = "/save")
    @RequiresPermissions("customerVisit:save")
    public Object save(@RequestBody CustomerVisit customerVisit) {
        HashMap<String, Object> params = new HashMap<>();
        if (customerVisit.getId() != null || customerVisit.getCustomerId() != null) {
            if (customerVisit.getVisitType() == 0) {
                customerVisit.setVisitType(2);
                params.put("customerTag", 2);
            } else {
                params.put("customerTag", customerVisit.getVisitType());
            }

        } else {
            params.put("customerTag", 2);
            if (customerVisit.getVisitType() == 0) {
                customerVisit.setVisitType(2);
            }
        }
        params.put("customerId", customerVisit.getCustomerId());
        customerVisitService.saveVisit(customerVisit);
        clienteleService.updateClienteleStatus(params);
        return R.ok();
    }

    @ApiOperation("用户详情")
    @GetMapping(value = "/info")
    public R info(@RequestParam Long id) {
        CustomerVisit customerVisit = customerVisitService.getByVisitId(id);
        if (ToolUtil.isEmpty(customerVisit)) {
            return R.fail("找不到该记录");
        }
        return R.ok(customerVisit);
    }
}
