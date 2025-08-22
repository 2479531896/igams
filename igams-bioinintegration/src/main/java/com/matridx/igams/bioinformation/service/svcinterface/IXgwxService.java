package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.XgwxDto;
import com.matridx.igams.bioinformation.dao.entities.XgwxModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;


public interface IXgwxService extends BaseBasicService<XgwxDto, XgwxModel> {
    /**
     * 根据taxids查询list
     */
     List<XgwxDto> getDtoListByIds(XgwxDto xgwxDto);

    /**
     * 查询list
     */
    List<XgwxDto> getXgwxList(XgwxDto xgwxDto);
}
