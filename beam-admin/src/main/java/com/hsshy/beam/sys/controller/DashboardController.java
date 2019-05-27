package com.hsshy.beam.sys.controller;
import com.hsshy.beam.common.constant.Constant;
import com.hsshy.beam.common.shiro.ShiroUtils;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.sys.entity.Clientele;
import com.hsshy.beam.sys.service.IClienteleService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 总览信息
 *
 * @author fengshuonan
 * @Date 2017年3月4日23:05:54
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private IClienteleService clienteleService;
    /**
     * 跳转到黑板
     */
    @RequiresPermissions("sys:dashboard:info")
    @GetMapping
    public Object blackboard() {

        return R.ok("首页");
    }
    @ApiOperation(value = "分页列表")
    @GetMapping(value = "/page/list")
    public Object pageList(Clientele clientele)  {
        if(ShiroUtils.getUserId().longValue()!= Constant.SUPER_ADMIN){
            clientele.setSalesAccount(ShiroUtils.getUserEntity().getAccount());
        }
        clientele.setCustomerTag(1);
        return  R.ok(clienteleService.selectPageList(clientele));
    }
}
