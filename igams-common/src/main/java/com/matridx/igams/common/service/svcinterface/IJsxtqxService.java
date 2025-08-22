package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.JsxtqxDto;
import com.matridx.igams.common.dao.entities.JsxtqxModel;

public interface IJsxtqxService extends BaseBasicService<JsxtqxDto, JsxtqxModel>{
    /*
        增加角色系统权限
     */
    boolean insertJsxtqxDto(JsxtqxDto jsxtqxDto);
}
