package com.hsshy.beam.web.base.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hsshy.beam.common.constant.DataBaseConstant;
import com.hsshy.beam.common.utils.ObjectUtils;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.web.base.entity.AbstractEntity;
import com.hsshy.beam.web.base.service.ICommonService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

public abstract class BaseCommonController<Entity extends AbstractEntity<ID>,ID extends Serializable>
        extends BaseBeanController<Entity>{

    private ICommonService<Entity> commonService;

    /**
     * 设置基础service
     *
     * @param commonService
     */
    @Autowired
    public void setCommonService(ICommonService<Entity> commonService) {
        this.commonService = commonService;
    }

    public Entity get(ID id) {
        if (!ObjectUtils.isNullOrEmpty(id)) {
            return commonService.getById(id);
        } else {
            return newModel();
        }
    }

    @ApiOperation(value = "查询(没有被逻辑删除)")
    @GetMapping(value = "/get/{id}")
    public Object getOne(@RequestBody @ApiParam(name="id",value="id",required=true) @PathVariable Long id) {
        Assert.notNull(id,"id不能为空");

        return R.ok(commonService.getById(id));
    }

    @ApiOperation(value = "查询(包括逻辑删除)")
    @GetMapping(value = "/logic/get/{id}")
    public Object getLogicOne(@RequestBody @ApiParam(name="id",value="id",required=true) @PathVariable Long id) {
        Assert.notNull(id,"id不能为空");
        return R.ok(commonService.getById(id));
    }

    @ApiOperation(value = "列表")
    @GetMapping(value = "/list")
    public List<Entity> list() {

       return commonService.list(new QueryWrapper<Entity>());
    }

    @ApiOperation(value = "分页列表")
    @GetMapping(value = "/page/list/{currentPage}")
    public Object pageList(@ApiParam(name="currentPage",value="currentPage",required=true) @PathVariable Integer currentPage) {

        return R.ok(commonService.page(new Page<Entity>(currentPage, DataBaseConstant.PAGE_SIZE), new QueryWrapper<Entity>()));
    }



    @ApiOperation(value = "新增")
    @PostMapping(value = "/add")
    public Object add(@RequestBody @ApiParam(name="实体对象",value="传入json格式",required=true) Entity entity) {
        Assert.notNull(entity,"数据不能为空");

        if(commonService.save(entity)){
            return R.ok("新增成功");
        }
        else {
            return R.ok("新增失败");

        }

    }

    @ApiOperation(value = "修改")
    @PutMapping(value = "/update")
    public Object update(@RequestBody @ApiParam(name="实体对象",value="传入json格式",required=true) Entity entity) {
        Assert.notNull(entity,"entity不能为空");
        if(commonService.save(entity)){
            return R.ok("修改成功");
        }
        else {
            return R.ok("修改失败");

        }
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    public Object delete(@RequestBody @ApiParam(name="实体对象",value="传入json格式",required=true) Entity entity) {
        Assert.notNull(entity,"数据不能为空");

        if(commonService.removeById(entity)){
            return R.ok("删除成功");
        }
        else {
            return R.ok("删除失败");

        }
    }





}
