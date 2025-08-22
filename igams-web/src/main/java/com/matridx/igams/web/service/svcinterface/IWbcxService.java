package com.matridx.igams.web.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.WbcxDto;
import com.matridx.igams.web.dao.entities.WbcxModel;

import java.util.List;

public interface IWbcxService extends BaseBasicService<WbcxDto, WbcxModel>{
    /**
     *获取名称
     * @return
     */
    List<WbcxDto> getSsgsList();
}
