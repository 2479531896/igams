package com.matridx.igams.production.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.HtfpmxDto;
import com.matridx.igams.production.dao.entities.HtfpmxModel;
import com.matridx.igams.production.dao.post.IHtfpmxDao;
import com.matridx.igams.production.service.svcinterface.IHtfpmxService;

import java.util.List;

@Service
public class HtfpmxServiceImpl extends BaseBasicServiceImpl<HtfpmxDto, HtfpmxModel, IHtfpmxDao> implements IHtfpmxService{

    /**
     * 合同发票明细保存
     */
    public void insertList(List<HtfpmxDto> list){
        dao.insertList(list);
    }
}
