package com.hsshy.beam.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsshy.beam.common.constant.Constant;
import com.hsshy.beam.common.shiro.ShiroUtils;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.common.utils.ToolUtil;
import com.hsshy.beam.sys.dao.ClienteleMapper;
import com.hsshy.beam.sys.entity.Clientele;
import com.hsshy.beam.sys.service.IClienteleService;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ClienteleServiceImpl extends ServiceImpl<ClienteleMapper, Clientele> implements IClienteleService {
    @Override
    public IPage<Clientele> selectPageList(Clientele clientele) {
        return baseMapper.selectPageList(new Page(clientele.getCurrentPage(),clientele.getPageSize()),clientele);
    }

    @Override
    public IPage<Clientele> selectMyPageList(Clientele clientele) {
        return baseMapper.selectMyPageList(new Page(clientele.getCurrentPage(),clientele.getPageSize()),clientele);
    }

    @Override
    public R saveClientele(Clientele clientele) {
        if(ToolUtil.isNotEmpty(clientele.getId())){
            clientele.setUpdatePersonId(ShiroUtils.getUserEntity().getAccount());
            clientele.setUpdatePersonName(ShiroUtils.getUserEntity().getName());
            clientele.setUpdateTime(new Date());
            Clientele clienteleEntity = baseMapper.getByClienteleId(clientele.getId());
            if(ToolUtil.isNotEmpty(clienteleEntity)){
                baseMapper.updateClientele(clientele);
            }
        }else {
            clientele.setCreatePersonId(ShiroUtils.getUserEntity().getAccount());
            clientele.setCreatePersonName(ShiroUtils.getUserEntity().getName());
            baseMapper.saveClientele(clientele);
        }
         return R.ok();
    }


    @Override
    public R deleteClientele(Long[] ids) {
        if(ToolUtil.isEmpty(ids)||ids.length<=0){
            return R.fail("未选择删除的角色");
        }
        for(Long id:ids) {
             baseMapper.delClienteleByid(id);
        }
        return R.ok();
    }

    @Override
    public R updateClientele(Long[] ids) {
        return null;
    }

    @Override
    public R addMyClient(Long[] ids) {
        if(ToolUtil.isEmpty(ids)||ids.length<=0){
            return R.fail("未选择添加的客户");
        }
        Map<String,Object> params=new HashMap<>();
        for(Long id:ids) {
            params.put("id",id);
            params.put("salesAccount",ShiroUtils.getUserEntity().getAccount());
            params.put("salesName",ShiroUtils.getUserEntity().getName());
            baseMapper.addMyClient(params);
            params.clear();
        }
        return R.ok();
    }

    @Override
    public R deleteMyClient(Long[] ids) {
        if(ToolUtil.isEmpty(ids)||ids.length<=0){
            return R.fail("未选择删除的客户");
        }
        Map<String,Object> params=new HashMap<>();
        for(Long id:ids) {
            params.put("id",id);
            params.put("salesAccount",ShiroUtils.getUserEntity().getAccount());
            params.put("salesName",ShiroUtils.getUserEntity().getName());
            baseMapper.deleteMyClient(params);
            params.clear();
        }
        return R.ok();
    }
}
