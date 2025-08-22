package com.matridx.igams.wechat.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.WlcstxDto;
import com.matridx.igams.wechat.dao.entities.WlcstxModel;
import com.matridx.igams.wechat.dao.post.IWlcstxDao;
import com.matridx.igams.wechat.service.svcinterface.IWlcstxService;

@Service
public class WlcstxServiceImpl extends BaseBasicServiceImpl<WlcstxDto, WlcstxModel, IWlcstxDao> implements IWlcstxService{
    /**
     * 新增或修改超时提醒数据
     * @param wlcstxDto
     * @return
     */
    @Override
    public boolean insertOrUpdateWlcstx(WlcstxDto wlcstxDto) {
        int result = dao.insertOrUpdateWlcstx(wlcstxDto);
        return result != 0;
    }
}
