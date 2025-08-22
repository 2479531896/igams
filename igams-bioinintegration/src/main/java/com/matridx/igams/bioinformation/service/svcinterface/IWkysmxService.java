package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.WkysmxDto;
import com.matridx.igams.bioinformation.dao.entities.WkysmxModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;


public interface IWkysmxService extends BaseBasicService<WkysmxDto, WkysmxModel>{
    /**
     * 批量新增
     */
    boolean insertList(List<WkysmxDto> list);

    List<WkysmxDto> getList(WkysmxDto wkysmxDto);
}
