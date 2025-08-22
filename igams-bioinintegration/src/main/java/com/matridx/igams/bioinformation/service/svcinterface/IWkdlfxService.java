package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.WkdlfxDto;
import com.matridx.igams.bioinformation.dao.entities.WkdlfxModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;


public interface IWkdlfxService extends BaseBasicService<WkdlfxDto, WkdlfxModel>{

    /**
     * 批量新增
     */
    boolean insertList(List<WkdlfxDto> list);

}
