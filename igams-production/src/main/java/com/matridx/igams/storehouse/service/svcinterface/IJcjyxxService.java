package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.JcjyxxDto;
import com.matridx.igams.storehouse.dao.entities.JcjyxxModel;

import java.util.List;


public interface IJcjyxxService extends BaseBasicService<JcjyxxDto, JcjyxxModel>{
    /**
     * 查询列表信息
     */
    List<JcjyxxDto> getDtoListInfo(JcjyxxDto jcjyxxDto);

    /**
     * 查询列表信息和明细信息
     */
    List<JcjyxxDto> getDtoListInfoAndMx(JcjyxxDto jcjyxxDto);

    boolean deleteByIds(List<String> ids);

    List<JcjyxxDto> getDtoListGroupByJyxx(JcjyxxDto jcjyxxDto);
}
