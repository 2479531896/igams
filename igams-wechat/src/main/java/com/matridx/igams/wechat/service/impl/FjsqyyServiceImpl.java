package com.matridx.igams.wechat.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.FjsqyyDto;
import com.matridx.igams.wechat.dao.entities.FjsqyyModel;
import com.matridx.igams.wechat.dao.post.IFjsqyyDao;
import com.matridx.igams.wechat.service.svcinterface.IFjsqyyService;

import java.util.List;

@Service
public class FjsqyyServiceImpl extends BaseBasicServiceImpl<FjsqyyDto, FjsqyyModel, IFjsqyyDao> implements IFjsqyyService{

    /**
     * 保存复检原因信息
     * @param list
     * @return
     */
    public boolean addDtoList(List<FjsqyyDto> list){
        return dao.addDtoList(list);
    }
}
