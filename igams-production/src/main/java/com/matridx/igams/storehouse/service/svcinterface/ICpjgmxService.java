package com.matridx.igams.storehouse.service.svcinterface;


import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.CpjgmxDto;
import com.matridx.igams.storehouse.dao.entities.CpjgmxModel;

import java.util.List;


public interface ICpjgmxService extends BaseBasicService<CpjgmxDto, CpjgmxModel>{

    /**
     * 批量新增
     */
    Boolean batchInsertDtoList(List<CpjgmxDto> cpjgmxDtos);

    /**
     * 批量修改
     */
    Boolean updateList(List<CpjgmxDto> cpjgmxDtos);
}
