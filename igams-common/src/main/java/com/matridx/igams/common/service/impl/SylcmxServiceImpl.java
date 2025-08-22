package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.dao.entities.SylcmxDto;
import com.matridx.igams.common.dao.entities.SylcmxModel;
import com.matridx.igams.common.dao.post.ISylcmxDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ISylcmxService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SylcmxServiceImpl extends BaseBasicServiceImpl<SylcmxDto, SylcmxModel, ISylcmxDao> implements ISylcmxService {

    @Override
    public boolean insertList(List<SylcmxDto> list) {
        return dao.insertList(list);
    }

    @Override
    public boolean delObsoleteData(SylcmxDto sylcmxDto) {
        return dao.delObsoleteData(sylcmxDto);
    }
}
