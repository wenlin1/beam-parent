package com.hsshy.beam.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.sys.entity.CustomerVisit;

public interface ICustomerVisitService extends IService<CustomerVisit> {
    IPage<CustomerVisit> selectPageList(CustomerVisit customerVisit);

    R saveVisit(CustomerVisit customerVisit);
    CustomerVisit getByVisitId(long id);
}
