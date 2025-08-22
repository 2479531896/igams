package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.SbpdmxDto;
import com.matridx.igams.production.dao.entities.SbpdmxModel;

import java.util.List;

public interface ISbpdmxService extends BaseBasicService<SbpdmxDto, SbpdmxModel>{

    /**
     * 批量新增
     */
    boolean insertList(List<SbpdmxDto> list);
    /**
     * 批量修改
     */
    boolean updateList(List<SbpdmxDto> list);

}
