package com.matridx.igams.wechat.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.KhyhzcbzDto;
import com.matridx.igams.wechat.dao.entities.KhyhzcbzModel;
import com.matridx.igams.wechat.dao.post.IKhyhzcbzDao;
import com.matridx.igams.wechat.service.svcinterface.IKhyhzcbzService;

import java.util.List;
import java.util.Map;

@Service
public class KhyhzcbzServiceImpl extends BaseBasicServiceImpl<KhyhzcbzDto, KhyhzcbzModel, IKhyhzcbzDao> implements IKhyhzcbzService{

    public boolean insertList(List<KhyhzcbzDto> list){
        return dao.insertList(list);
    }

    public boolean updateList(List<KhyhzcbzDto> list){
        return dao.updateList(list);
    }

    public boolean delInfo(KhyhzcbzDto khyhzcbzDto){
        return dao.delInfo(khyhzcbzDto);
    }

    public List<KhyhzcbzDto> getYhzcByXmcss(KhyhzcbzDto khyhzcbzDto){
        return dao.getYhzcByXmcss(khyhzcbzDto);
    }

    public List<KhyhzcbzDto> getYhzcByXmje(KhyhzcbzDto khyhzcbzDto){
        return dao.getYhzcByXmje(khyhzcbzDto);
    }

    public List<KhyhzcbzDto> getYhzcByDcsl(KhyhzcbzDto khyhzcbzDto){
        return dao.getYhzcByDcsl(khyhzcbzDto);
    }

    public List<Map<String,Object>> getFinalListByXmcss(KhyhzcbzDto khyhzcbzDto){
        return dao.getFinalListByXmcss(khyhzcbzDto);
    }
}
