package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.WlcstxDto;
import com.matridx.igams.wechat.dao.entities.WlcstxModel;

public interface IWlcstxService extends BaseBasicService<WlcstxDto, WlcstxModel>{
    /**
     * 新增或修改物流超时提醒数据
     * @param wlcstxDto
     */
    boolean insertOrUpdateWlcstx(WlcstxDto wlcstxDto);
}
