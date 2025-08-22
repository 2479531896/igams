package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.GgglDto;
import com.matridx.igams.common.dao.entities.GgglModel;

public interface IGgglService extends BaseBasicService<GgglDto, GgglModel>{
    /*
        保存公告
     */
    boolean addSaveNotice(GgglDto ggglDto) throws BusinessException;
    /*
        修改保存公告
     */
    boolean modSaveNotice(GgglDto ggglDto) throws BusinessException;
}
