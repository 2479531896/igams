package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.FjsqyyDto;
import com.matridx.igams.wechat.dao.entities.FjsqyyModel;

import java.util.List;

public interface IFjsqyyService extends BaseBasicService<FjsqyyDto, FjsqyyModel>{

    /**
     * 保存复检原因信息
     * @param list
     * @return
     */
    boolean addDtoList(List<FjsqyyDto> list);
}
