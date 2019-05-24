package com.hsshy.beam.sys.controller;

import com.hsshy.beam.common.base.controller.BaseController;
import com.hsshy.beam.common.constant.Constant;
import com.hsshy.beam.common.shiro.ShiroUtils;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.sys.entity.Clientele;
import com.hsshy.beam.sys.service.IClienteleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value="MyClientController",tags={"我的客户接口"})
@RequestMapping("/sys/myclient")
@RestController
public class MyClientController extends BaseController {
    @Autowired
    private IClienteleService clienteleService;

    @RequiresPermissions("sys:myclient:list")
    @ApiOperation(value = "分页列表")
    @GetMapping(value = "/page/list")
    public Object pageList(Clientele clientele)  {
        if(ShiroUtils.getUserId().longValue()!= Constant.SUPER_ADMIN){
            clientele.setSalesAccount(ShiroUtils.getUserEntity().getAccount());
        }
        return  R.ok(clienteleService.selectPageList(clientele));
    }

    /**
     * 查询可导入我的客户
     * @param clientele
     * @returnmy
     */
    @RequiresPermissions("sys:myclient:mylist")
    @ApiOperation(value = "可导入我的客户分页列表")
    @GetMapping(value = "/page/mylist")
    public Object pagemyList(Clientele clientele)  {
        if(ShiroUtils.getUserId().longValue()!= Constant.SUPER_ADMIN){
            clientele.setSalesAccount(ShiroUtils.getUserEntity().getAccount());
        }
        return  R.ok(clienteleService.selectMyPageList(clientele));
    }

    @ApiOperation("保存用户")
    @PostMapping(value = "/save")
    @RequiresPermissions("sys:myclient:save")
    public Object save(@RequestBody Clientele clientele){
        clienteleService.saveClientele(clientele);
        return R.ok();
    }

    @ApiOperation("导入我的客户")
    @PostMapping(value = "/addMyClient")
    @RequiresPermissions("sys:myclient:addMyClient")
    public Object addMyClient(@RequestBody Long addIds[]){
        clienteleService.addMyClient(addIds);
        return R.ok();
    }

    @RequiresPermissions("sys:myclient:deleteMyClient")
    @ApiOperation("删除我的客户")
    @PostMapping(value = "/deleteMyClient")
    public Object deleteMyClient(@RequestBody Long ids[]){
        return clienteleService.deleteMyClient(ids);
    }
}
