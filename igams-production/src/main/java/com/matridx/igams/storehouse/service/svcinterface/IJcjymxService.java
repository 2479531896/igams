package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.JcjymxDto;
import com.matridx.igams.storehouse.dao.entities.JcjymxModel;

import java.util.List;


public interface IJcjymxService extends BaseBasicService<JcjymxDto, JcjymxModel>{
    /**
     * 根据仓库分组查询
     */
    List<JcjymxDto> getDtoListGroupByCk(JcjymxDto jcjymxDto);

    /**
     * 根据借出借用信息id分组查询
     */
    List<JcjymxDto> getDtoListGroupByXx(JcjymxDto jcjymxDto);

    boolean deleteByIds(List<String> ids);

    List<JcjymxDto> getScphAndSlByJyxxid(String ywid);
    List<JcjymxDto> getDtoListGroupBy(JcjymxDto jcjymxDto);
    boolean updateList(List<JcjymxDto> jcjymxDtos);
}
