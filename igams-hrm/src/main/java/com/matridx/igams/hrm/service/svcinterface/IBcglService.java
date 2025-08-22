package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.BcglDto;
import com.matridx.igams.hrm.dao.entities.BcglModel;

public interface IBcglService extends BaseBasicService<BcglDto, BcglModel>{
    /**
     * 同步修改保存
     */
    boolean synchronousSavescheduling(User user);
    /**
     * @description 通过出勤时间获取班次信息
     */
    BcglDto getBcByCqsj(String cqsj);
}
