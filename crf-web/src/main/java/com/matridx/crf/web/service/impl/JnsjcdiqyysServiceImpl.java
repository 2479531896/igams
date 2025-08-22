package com.matridx.crf.web.service.impl;

import com.matridx.crf.web.dao.entities.JnsjcdiqyysDto;
import com.matridx.crf.web.dao.entities.JnsjcdiqyysModel;
import com.matridx.crf.web.dao.post.IJnsjcdiqyysDao;
import com.matridx.crf.web.service.svcinterface.IJnsjcdiqyysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;

import java.util.List;


@Service
public class JnsjcdiqyysServiceImpl extends BaseBasicServiceImpl<JnsjcdiqyysDto, JnsjcdiqyysModel, IJnsjcdiqyysDao> implements IJnsjcdiqyysService {

    @Autowired
    private IJnsjcdiqyysDao jnsjcdiqyysDao;

    @Override
    public boolean insertList(List<JnsjcdiqyysDto> list) {
        return jnsjcdiqyysDao.insertList(list)>0;
    }
}
