package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.YglzxxDto;
import com.matridx.igams.hrm.dao.entities.YglzxxModel;


public interface IYglzxxService extends BaseBasicService<YglzxxDto, YglzxxModel> {


    boolean yglzupholdSaveRoster(YglzxxDto yglzxxDto) throws BusinessException;
}
