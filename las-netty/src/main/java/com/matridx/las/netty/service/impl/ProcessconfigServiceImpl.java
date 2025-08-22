package com.matridx.las.netty.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;

import com.matridx.las.netty.dao.entities.ProcessconfigDto;
import com.matridx.las.netty.dao.entities.ProcessconfigModel;
import com.matridx.las.netty.dao.post.IProcessconfigDao;
import com.matridx.las.netty.service.svcinterface.IProcessconfigService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessconfigServiceImpl extends BaseBasicServiceImpl<ProcessconfigDto, ProcessconfigModel, IProcessconfigDao> implements IProcessconfigService {
    /**
     * 查找一个流程需要的引导
     * @param csid
     * @return
     */
    @Override
    public List<ProcessconfigDto> getDtoByCsid(String csid) {
        return dao.getDtoByCsid(csid);
    }
}
