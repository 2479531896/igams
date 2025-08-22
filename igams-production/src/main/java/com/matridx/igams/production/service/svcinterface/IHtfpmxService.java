package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.HtfpmxDto;
import com.matridx.igams.production.dao.entities.HtfpmxModel;

import java.util.List;

public interface IHtfpmxService extends BaseBasicService<HtfpmxDto, HtfpmxModel>{

    /**
     * 合同发票明细保存
     */
    void insertList(List<HtfpmxDto> list);
}
