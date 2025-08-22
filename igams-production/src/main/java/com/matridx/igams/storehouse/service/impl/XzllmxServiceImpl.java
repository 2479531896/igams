package com.matridx.igams.storehouse.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.XzllmxDto;
import com.matridx.igams.storehouse.dao.entities.XzllmxModel;
import com.matridx.igams.storehouse.dao.post.IXzllmxDao;
import com.matridx.igams.storehouse.service.svcinterface.IXzllmxService;

import java.util.List;

@Service
public class XzllmxServiceImpl extends BaseBasicServiceImpl<XzllmxDto, XzllmxModel, IXzllmxDao> implements IXzllmxService{

    /**
     * 获取行政领料明细信息
     * @param xzllid
     * @return
     */
    public List<XzllmxDto> getDtoXzllmxListByXzllid(String xzllid) {
        return dao.getDtoXzllmxListByXzllid(xzllid);
    }

    @Override
    public boolean insertList(List<XzllmxDto> xzllmxDtos) {
        return dao.insertList(xzllmxDtos);
    }
    @Override
    public boolean updateList(List<XzllmxDto> xzllmxDtos) {
        return dao.updateList(xzllmxDtos);
    }
    @Override
    public boolean delXzllmxByXzllid(XzllmxDto xzllmxDto) {
        return dao.delXzllmxByXzllid(xzllmxDto);
    }

    @Override
    public List<XzllmxDto> getDtoXzllmxListByXzkcid(String xzkcid) {
        return dao.getDtoXzllmxListByXzkcid(xzkcid);
    }
}
