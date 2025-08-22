package com.matridx.server.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.FjsqyyDto;
import com.matridx.server.wechat.dao.entities.FjsqyyModel;

import java.util.List;

public interface IFjsqyyService extends BaseBasicService<FjsqyyDto, FjsqyyModel>{

    /**
     * 保存复检原因信息
     * @param list
     * @return
     */
    boolean addDtoList(List<FjsqyyDto> list);
}
