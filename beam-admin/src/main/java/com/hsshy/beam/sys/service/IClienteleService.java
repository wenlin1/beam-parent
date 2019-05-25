package com.hsshy.beam.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.sys.entity.Clientele;

import java.util.List;

public interface IClienteleService extends IService<Clientele> {
    IPage<Clientele> selectPageList(Clientele clientele);

    IPage<Clientele> selectMyPageList(Clientele clientele);

    R saveClientele(Clientele clientele);

    R deleteClientele(Long ids[]);

    R updateClientele(Long ids[]);

    R addMyClient(Long ids[]);
    R deleteMyClient(Long ids[]);
    List<Clientele> clinetList();

}
