package com.matridx.igams.wechat.service.impl;


import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.BfdxlxrglDto;
import com.matridx.igams.wechat.dao.entities.BfdxlxrglModel;
import com.matridx.igams.wechat.dao.post.IBfdxlxrglDao;
import com.matridx.igams.wechat.service.svcinterface.IBfdxlxrglService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BfdxlxrglServiceImpl extends BaseBasicServiceImpl<BfdxlxrglDto, BfdxlxrglModel, IBfdxlxrglDao> implements IBfdxlxrglService {

    @Override
    public boolean insertList(List<BfdxlxrglDto> list) {
        return dao.insertList(list);
    }

    @Override
    public boolean updateList(List<BfdxlxrglDto> list) {
        return dao.updateList(list);
    }
    @Override
    public boolean mergeList(List<BfdxlxrglDto> list){
        return dao.mergeList(list);
    }
}
