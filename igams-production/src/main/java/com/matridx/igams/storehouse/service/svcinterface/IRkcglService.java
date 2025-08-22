package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.RkcglDto;
import com.matridx.igams.storehouse.dao.entities.RkcglModel;

import java.util.List;

public interface IRkcglService extends BaseBasicService<RkcglDto, RkcglModel>{
    /**
     * 查询入库类别
     * @param
     * @return
     */
    List<RkcglDto> getDtoRklb(String ryid);
}
