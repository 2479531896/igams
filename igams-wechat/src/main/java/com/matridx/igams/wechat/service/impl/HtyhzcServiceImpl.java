package com.matridx.igams.wechat.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.HtyhzcDto;
import com.matridx.igams.wechat.dao.entities.HtyhzcModel;
import com.matridx.igams.wechat.dao.post.IHtyhzcDao;
import com.matridx.igams.wechat.service.svcinterface.IHtyhzcService;

import java.util.List;

@Service
public class HtyhzcServiceImpl extends BaseBasicServiceImpl<HtyhzcDto, HtyhzcModel, IHtyhzcDao> implements IHtyhzcService{

    public boolean insertList(List<HtyhzcDto> htyhzcDtos){
        return dao.insertList(htyhzcDtos);
    }

    public boolean updateList(List<HtyhzcDto> htyhzcDtos){
        return dao.updateList(htyhzcDtos);
    }

    public List<HtyhzcDto> getYhzcInfo(HtyhzcDto htyhzcDto){
        return dao.getYhzcInfo(htyhzcDto);
    }
}
