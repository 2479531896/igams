package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.TkglDto;
import com.matridx.igams.hrm.dao.entities.TkglModel;

public interface ITkglService extends BaseBasicService<TkglDto, TkglModel> {
    /*
    * 修改题库
    * */
    boolean updateTK(TkglDto tkglDto) throws BusinessException;
}
