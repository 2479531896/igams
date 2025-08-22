package com.matridx.igams.wechat.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.WzysxxDto;
import com.matridx.igams.wechat.dao.entities.WzysxxModel;
import com.matridx.igams.wechat.dao.post.IWzysxxDao;
import com.matridx.igams.wechat.service.svcinterface.IWzysxxService;

import java.util.List;

/**
 * 物种原始信息
 */
@Service
public class WzysxxServiceImpl extends BaseBasicServiceImpl<WzysxxDto, WzysxxModel, IWzysxxDao> implements IWzysxxService{

    public boolean insertDtoList(List<WzysxxDto> wzysxxDtos){
        return dao.insertDtoList(wzysxxDtos);
    }

    public List<WzysxxDto> getLlh(WzysxxDto wzysxxDto){
        return dao.getLlh(wzysxxDto);
    }

    public List<WzysxxDto> getDtoList(WzysxxDto wzysxxDto){
        return dao.getDtoList(wzysxxDto);
    }
}
