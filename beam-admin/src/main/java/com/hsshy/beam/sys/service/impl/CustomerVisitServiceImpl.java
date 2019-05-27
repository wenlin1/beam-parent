package com.hsshy.beam.sys.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsshy.beam.common.shiro.ShiroUtils;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.common.utils.ToolUtil;
import com.hsshy.beam.sys.dao.CustomerVisitMapper;
import com.hsshy.beam.sys.entity.CustomerVisit;
import com.hsshy.beam.sys.service.ICustomerVisitService;
import org.springframework.stereotype.Service;

@Service
public class CustomerVisitServiceImpl  extends ServiceImpl<CustomerVisitMapper, CustomerVisit> implements ICustomerVisitService {
    @Override
    public IPage<CustomerVisit> selectPageList(CustomerVisit customerVisit) {
        if(ToolUtil.isNotEmpty(ShiroUtils.getUserEntity().getId())) {
            customerVisit.setUserId(ShiroUtils.getUserEntity().getId());
        }
        return baseMapper.selectPageList(new Page(customerVisit.getCurrentPage(),customerVisit.getPageSize()),customerVisit);
    }

    @Override
    public R saveVisit(CustomerVisit customerVisit) {
        if(ToolUtil.isNotEmpty(ShiroUtils.getUserEntity().getId())) {
            customerVisit.setUserId(ShiroUtils.getUserEntity().getId());
        }
        if(ToolUtil.isNotEmpty(customerVisit.getId())){
            baseMapper.updateVisit(customerVisit);
        }else {
            baseMapper.saveVisit(customerVisit);
        }
        return R.ok();
    }

    @Override
    public CustomerVisit getByVisitId(long id) {
       return baseMapper.getByVisitId(id);
    }
}
