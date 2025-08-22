package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.WklcznDto;
import com.matridx.igams.bioinformation.dao.entities.WklcznModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;


public interface IWklcznService extends BaseBasicService<WklcznDto, WklcznModel>{

    /**
     * 批量新增
     */
    boolean insertList(List<WklcznDto> list);

}
