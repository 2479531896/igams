package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.HbxxzDto;
import com.matridx.igams.wechat.dao.entities.HbxxzModel;
import com.matridx.igams.wechat.dao.post.IHbxxzDao;
import com.matridx.igams.wechat.service.svcinterface.IHbxxzService;
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
