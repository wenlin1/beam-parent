package com.hsshy.beam.sys.dao;
import com.hsshy.beam.common.base.dto.TreeNode;
import com.hsshy.beam.sys.entity.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hsshy.beam.sys.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门管理
 * 
 * @author hs
 * @email 457030599@qq.com
 * @date 2018-12-17 15:21:00
 */
public interface DeptMapper extends BaseMapper<Dept> {

    IPage<Dept> selectPageList(Page page, @Param("dept") Dept dept);

    List<Dept> queryListParentId(Long parentId);



}
