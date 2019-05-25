package com.hsshy.beam.sys.controller;

import com.hsshy.beam.common.base.controller.BaseController;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.sys.entity.Clientele;
import com.hsshy.beam.sys.entity.User;
import com.hsshy.beam.sys.service.IClienteleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 客户管理表
 *
 * @author WL
 * @email 1102464140@qq.com
 * @date 2019-05-21 18:03:20
 */
@Api(value="ClienteleController",tags={"客户信息接口"})
@RequestMapping("/clientele")
@RestController
public class ClienteleController  extends BaseController {
    @Autowired
    private IClienteleService clienteleService;

    @RequiresPermissions("clientele:list")
    @ApiOperation(value = "分页列表")
    @GetMapping(value = "/page/list")
    public Object pageList(Clientele clientele)  {
        return  R.ok(clienteleService.selectPageList(clientele));
    }

    @ApiOperation("保存用户")
    @PostMapping(value = "/save")
    @RequiresPermissions("clientele:save")
    public Object save(@RequestBody Clientele clientele){
      clienteleService.saveClientele(clientele);
      return R.ok();
    }

    @RequiresPermissions("clientele:del")
    @ApiOperation("批量删除用户")
    @PostMapping(value = "/delete")
    public Object delete(@RequestBody Long ids[]){
        return clienteleService.deleteClientele(ids);
    }
}
