package com.matridx.las.netty.service.impl;

import com.matridx.las.netty.dao.entities.MlpzDto;
import com.matridx.las.netty.dao.entities.MlpzModel;
import com.matridx.las.netty.dao.post.IMlpzDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.las.netty.service.svcinterface.IMlpzService;

import java.util.List;

@Service
public class MlpzServiceImpl extends BaseBasicServiceImpl<MlpzDto, MlpzModel, IMlpzDao> implements IMlpzService {
    @Autowired
    private IMlpzDao mlpzDao;

    public List<MlpzDto> getMlpzList(MlpzDto mlpzDto) {
        return  dao.getMlpzList(mlpzDto);
    }

}
