package com.hsshy.beam.sys.controller;
import com.hsshy.beam.common.base.controller.BaseController;
import com.hsshy.beam.common.factory.impl.ConstantFactory;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.common.utils.ToolUtil;
import com.hsshy.beam.sys.dto.ChangePassowdForm;
import com.hsshy.beam.sys.entity.Clientele;
import com.hsshy.beam.sys.entity.Dept;
import com.hsshy.beam.sys.entity.User;
import com.hsshy.beam.sys.service.IDeptService;
import com.hsshy.beam.sys.service.IUserService;
import com.hsshy.beam.sys.wrapper.UserWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员表
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2018-10-07 18:03:20
 */
@Api(value="UserController",tags={"User接口"})
@RequestMapping("/sys/user")
@RestController
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IDeptService deptService;


    @ApiOperation(value = "分页列表")
    @GetMapping(value = "/page/list")
    @RequiresPermissions("sys:user:list")
    public R pageList(User user)  {

        return  R.ok(new UserWrapper(userService.selectPageList(user)).wrap());
    }

    @ApiOperation("改变状态,是否可用")
    @PostMapping(value = "/change/status/{flag}")
    public R changeStatus(@RequestBody Long userId,@PathVariable Integer flag){
       return userService.changeStatus(userId,flag);
    }

    @ApiOperation("保存用户")
    @PostMapping(value = "/save")
    @RequiresPermissions("sys:user:save")
    public R save(@RequestBody User user){
        //删除缓存

        return userService.saveUser(user);
    }

    @ApiOperation("批量删除用户")
    @PostMapping(value = "/delete")
    @RequiresPermissions("sys:user:del")
    public R delete(@RequestBody Long userIds[]){
        return userService.deleteUser(userIds);
    }

    @ApiOperation("用户详情")
    @GetMapping(value = "/info")
    public R info(@RequestParam Long userId){
        User user = userService.getById(userId);
        if(ToolUtil.isEmpty(user)){
            return R.fail("找不到该用户");
        }
        List<Long> roleIds = ConstantFactory.me().getRoleIdsById(userId);
        user.setRoleIds(roleIds);
        Dept dept = deptService.getById(user.getDeptId());
        if(ToolUtil.isNotEmpty(dept)){
            user.setDeptName(dept.getName());

        }
        return R.ok(user);
    }

    @ApiOperation("重置用户密码")
    @PostMapping(value = "/reset/password")
    @RequiresPermissions("sys:user:resetPassword")
    public R resetPassword(@RequestBody Long userIds[]){
        return userService.resetPassword(userIds);
    }

    @ApiOperation("修改密码")
    @RequiresPermissions("sys:user:changePassword")
    @PostMapping(value = "/change/password")
    public R changePassword(@RequestBody ChangePassowdForm changePassowdForm){

        return userService.changePassword(changePassowdForm);
    }

    @ApiOperation(value = "获取我的客户分页列表")
    @GetMapping(value = "/userList")
    public Object userList(User user)  {
        return  R.ok(userService.userList());
    }





}