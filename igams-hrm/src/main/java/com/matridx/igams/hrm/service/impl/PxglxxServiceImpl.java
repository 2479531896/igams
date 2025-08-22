package com.matridx.igams.hrm.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.PxglxxDto;
import com.matridx.igams.hrm.dao.entities.PxglxxModel;
import com.matridx.igams.hrm.dao.post.IPxglxxDao;
import com.matridx.igams.hrm.service.svcinterface.IPxglxxService;

import java.util.List;

@Service
public class PxglxxServiceImpl extends BaseBasicServiceImpl<PxglxxDto, PxglxxModel, IPxglxxDao> implements IPxglxxService{

    @Override
    public boolean insertPxglxxDtos(List<PxglxxDto> pxglxxDtos) {
        return dao.insertPxglxxDtos(pxglxxDtos);
    }

    @Override
    public List<PxglxxDto> getTrainByWj(PxglxxDto pxglxxDto) {
        return dao.getTrainByWj(pxglxxDto);
    }
}
