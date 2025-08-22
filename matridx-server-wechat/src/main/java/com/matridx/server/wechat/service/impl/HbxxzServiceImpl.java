package com.matridx.server.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.HbxxzDto;
import com.matridx.server.wechat.dao.entities.HbxxzModel;
import com.matridx.server.wechat.dao.post.IHbxxzDao;
import com.matridx.server.wechat.service.svcinterface.IHbxxzService;
import org.springframework.stereotype.Service;

@Service
public class HbxxzServiceImpl extends BaseBasicServiceImpl<HbxxzDto, HbxxzModel, IHbxxzDao> implements IHbxxzService {

    /**
     * @param hbxxzDto
     * @return
     */
    @Override
    public HbxxzDto getSameDto(HbxxzDto hbxxzDto) {
        return dao.getSameDto(hbxxzDto);
    }
}
