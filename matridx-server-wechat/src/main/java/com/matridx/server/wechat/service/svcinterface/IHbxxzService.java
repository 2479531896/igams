package com.matridx.server.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.HbxxzDto;
import com.matridx.server.wechat.dao.entities.HbxxzModel;

public interface IHbxxzService extends BaseBasicService<HbxxzDto, HbxxzModel>{

    public HbxxzDto getSameDto(HbxxzDto hbxxzDto);
}
