package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.YsmxDto;
import com.matridx.igams.production.dao.entities.YsmxModel;

import java.util.List;

public interface IYsmxService extends BaseBasicService<YsmxDto, YsmxModel>{

    /**
     * 批量新增
     */
    boolean insertList(List<YsmxDto> list);

    /**
     * 删除废弃数据
     */
    boolean delObsoleteData(YsmxDto ysmxDto);

}
