package com.hsshy.beam.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hsshy.beam.sys.entity.Clientele;
import org.apache.ibatis.annotations.Param;


public interface ClienteleMapper extends BaseMapper<Clientele> {
    IPage<Clientele> selectPageList(Page page, @Param("clientele") Clientele clientele);

    void saveClientele(@Param("clientele") Clientele clientele);

    void delClienteleByid(Long id);
}
