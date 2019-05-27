package com.hsshy.beam.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hsshy.beam.sys.entity.CustomerVisit;
import org.apache.ibatis.annotations.Param;

public interface CustomerVisitMapper  extends BaseMapper<CustomerVisit> {
    IPage<CustomerVisit> selectPageList(Page page, @Param("customerVisit") CustomerVisit customerVisit);
    void saveVisit(@Param("customerVisit") CustomerVisit customerVisit);

    void updateVisit(@Param("customerVisit") CustomerVisit customerVisit);

    CustomerVisit getByVisitId(long id);
}
