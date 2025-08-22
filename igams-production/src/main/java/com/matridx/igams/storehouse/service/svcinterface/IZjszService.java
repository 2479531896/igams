package com.matridx.igams.storehouse.service.svcinterface;


import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.ZjszDto;
import com.matridx.igams.storehouse.dao.entities.ZjszModel;

import java.util.Map;

public interface IZjszService extends BaseBasicService<ZjszDto, ZjszModel> {

    Map<String,Object> insertQualitySetting(ZjszDto zjszDto) throws BusinessException;

    boolean delQualitySetting(ZjszDto zjszDto) throws BusinessException;
}
