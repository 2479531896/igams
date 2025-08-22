package com.matridx.las.netty.service.impl;

import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.las.netty.dao.entities.SjlcModel;
import com.matridx.las.netty.dao.post.ISjlcDao;
import com.matridx.las.netty.service.svcinterface.ISjlcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;

import java.util.List;

@Service
public class SjlcServiceImpl extends BaseBasicServiceImpl<SjlcDto, SjlcModel, ISjlcDao> implements ISjlcService {
    @Autowired
    private ISjlcDao sjlcDao;
    @Override
    public List<SjlcDto> getSjlcList(SjlcDto sjlcDto){
            return sjlcDao.getSjlcList(sjlcDto);
    }
}
