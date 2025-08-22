package com.matridx.igams.hrm.service.impl;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.hrm.dao.entities.HbszmxDto;
import com.matridx.igams.hrm.dao.entities.HbszmxModel;
import com.matridx.igams.hrm.dao.post.IHbszmxDao;
import com.matridx.igams.hrm.service.svcinterface.IHbszmxService;
import com.matridx.igams.common.util.DingTalkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HbszmxServiceImpl extends BaseBasicServiceImpl<HbszmxDto, HbszmxModel, IHbszmxDao> implements IHbszmxService {

    @Override
    public boolean insertHbszmxDtos(List<HbszmxDto> hbszmxDtos) {
        return dao.insertHbszmxDtos(hbszmxDtos);
    }

    @Override
    public boolean updateHbszmxDtos(List<HbszmxDto> hbszmxDtos) {
        return dao.updateHbszmxDtos(hbszmxDtos);
    }

    @Override
    public boolean deleteByHbmxids(HbszmxDto hbszmxDto) {
        return dao.deleteByHbmxids(hbszmxDto);
    }

    @Override
    public void updateHbszmxSysl(List<HbszmxDto> hbszmxDtos) {
        dao.updateHbszmxSysl(hbszmxDtos);
    }
}
