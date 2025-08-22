package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.WknyfxDto;
import com.matridx.igams.bioinformation.dao.entities.WknyfxModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;


public interface IWknyfxService extends BaseBasicService<WknyfxDto, WknyfxModel>{

    /**
     * 批量新增
     */
    boolean insertList(List<WknyfxDto> list);

}
