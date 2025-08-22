package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.HbxxzDto;
import com.matridx.igams.wechat.dao.entities.HbxxzModel;

public interface IHbxxzService extends BaseBasicService<HbxxzDto, HbxxzModel>{

    public HbxxzDto getSameDto(HbxxzDto hbxxzDto);
}
