package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.YghtxxDto;
import com.matridx.igams.hrm.dao.entities.YghtxxModel;


public interface IYghtxxService extends BaseBasicService<YghtxxDto, YghtxxModel> {

    /**
     * 员工合同信息维护
     */
    boolean htupholdSaveRoster(YghtxxDto yghtxxDto) throws BusinessException;
}
