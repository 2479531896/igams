package com.matridx.las.netty.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.netty.dao.entities.MlpzDto;
import com.matridx.las.netty.dao.entities.MlpzModel;

import java.util.List;

public interface IMlpzService extends BaseBasicService<MlpzDto, MlpzModel>{
    public List<MlpzDto> getMlpzList(MlpzDto mlpzDto);
}
