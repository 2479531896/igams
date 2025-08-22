package com.matridx.igams.storehouse.service.impl;


import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.RkcglDto;
import com.matridx.igams.storehouse.dao.entities.RkcglModel;
import com.matridx.igams.storehouse.dao.post.IRkcglDao;
import com.matridx.igams.storehouse.service.svcinterface.IRkcglService;

import java.util.List;

@Service
public class RkcglServiceImpl extends BaseBasicServiceImpl<RkcglDto, RkcglModel, IRkcglDao> implements IRkcglService{

    @Override
    public List<RkcglDto> getDtoRklb(String ryid) {
        return dao.getDtoRklb(ryid);
    }
}
