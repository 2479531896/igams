package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.JcghmxDto;
import com.matridx.igams.storehouse.dao.entities.JcghmxModel;

import java.util.List;


public interface IJcghmxService extends BaseBasicService<JcghmxDto, JcghmxModel>{

    /**
     * 通过jyxxid获取信息
     * @param
     * @return
     */
    List<JcghmxDto> getListByJyxxid(JcghmxDto jcghmxDto);

    /**
     * 通过ckid分组获取信息
     * @param
     * @return
     */
    List<JcghmxDto> getDtoListGroupByCk(JcghmxDto jcghmxDto);
    /**
     * 批量新增
     * @param jcghmxDtos
     * @return
     */
    boolean insertList(List<JcghmxDto> jcghmxDtos);


}
