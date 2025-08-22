package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.BommxDto;
import com.matridx.igams.production.dao.entities.BommxModel;
import com.matridx.igams.production.dao.post.IScbommxDao;
import com.matridx.igams.production.service.svcinterface.IScbommxService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScbommxServiceImpl extends BaseBasicServiceImpl<BommxDto, BommxModel, IScbommxDao> implements IScbommxService {

    @Override
    public boolean insertBommxDtos(List<BommxDto> bommxDtos) {
        return dao.insertBommxDtos(bommxDtos);
    }

    @Override
    public boolean updateBommxDtos(List<BommxDto> bommxDtos) {
        return dao.updateBommxDtos(bommxDtos);
    }

    @Override
    public boolean deleteByBomIds(BommxDto bommxDto) {
        return dao.deleteByBomIds(bommxDto);
    }

    @Override
    public List<BommxDto> getProduceReceiveBom(BommxDto bommxDto) {
        return dao.getProduceReceiveBom(bommxDto);
    }
}
