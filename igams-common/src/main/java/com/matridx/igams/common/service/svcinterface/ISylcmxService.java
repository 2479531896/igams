package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.SylcmxDto;
import com.matridx.igams.common.dao.entities.SylcmxModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface ISylcmxService extends BaseBasicService<SylcmxDto, SylcmxModel>{

    /**
     * 批量新增
     */
    boolean insertList(List<SylcmxDto> list);

    /**
     * 删除废弃数据
     */
    boolean delObsoleteData(SylcmxDto sylcmxDto);

}
