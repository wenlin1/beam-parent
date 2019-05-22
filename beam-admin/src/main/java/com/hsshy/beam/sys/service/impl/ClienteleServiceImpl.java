package com.hsshy.beam.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsshy.beam.common.shiro.ShiroUtils;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.common.utils.ToolUtil;
import com.hsshy.beam.sys.dao.ClienteleMapper;
import com.hsshy.beam.sys.entity.Clientele;
import com.hsshy.beam.sys.service.IClienteleService;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class ClienteleServiceImpl extends ServiceImpl<ClienteleMapper, Clientele> implements IClienteleService {
    @Override
    public IPage<Clientele> selectPageList(Clientele clientele) {
        return baseMapper.selectPageList(new Page(clientele.getCurrentPage(),clientele.getPageSize()),clientele);
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
}
