package com.matridx.igams.web.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.JszyczbDto;
import com.matridx.igams.web.dao.entities.JszyczbModel;

import java.util.List;

public interface IJszyczbService extends BaseBasicService<JszyczbDto, JszyczbModel>{

    /**
     * 根据角色ID获取该角色全部菜单权限
     * @param jsid
     * @return
     */
    List<JszyczbDto> getListById(String jsid);

}
