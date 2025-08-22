package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.BtglDto;
import com.matridx.igams.hrm.dao.entities.BtglModel;

public interface IBtglService extends BaseBasicService<BtglDto, BtglModel>{
    /**
     * 新增补贴保存
     */
    boolean addSaveSubsidys(BtglDto btglDto, User user);
}
