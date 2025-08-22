package com.matridx.las.netty.service.impl;

import com.matridx.las.netty.dao.entities.YqxxinfosxDto;
import com.matridx.las.netty.dao.entities.YqxxinfosxModel;
import com.matridx.las.netty.dao.post.IYqxxinfosxDao;
import com.matridx.las.netty.service.svcinterface.IYqxxinfosxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;

import java.util.List;


@Service
public class YqxxinfosxServiceImpl extends BaseBasicServiceImpl<YqxxinfosxDto, YqxxinfosxModel, IYqxxinfosxDao> implements IYqxxinfosxService {
    @Autowired
    private IYqxxinfosxDao yqxxinfosxDao;

    public void deleteAll() {
        yqxxinfosxDao.deleteAll();
    }

    public List<YqxxinfosxDto> getYqxxinfoSxList(YqxxinfosxDto yqxxinfosxDto){
        return  yqxxinfosxDao.getYqxxinfoSxList(yqxxinfosxDto);
    }

}
