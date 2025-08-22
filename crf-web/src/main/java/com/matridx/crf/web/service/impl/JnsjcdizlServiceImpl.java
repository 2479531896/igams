package com.matridx.crf.web.service.impl;

import com.matridx.crf.web.dao.entities.JnsjcdizlDto;
import com.matridx.crf.web.dao.entities.JnsjcdizlModel;
import com.matridx.crf.web.dao.post.IJnsjcdizlDao;
import com.matridx.crf.web.service.svcinterface.IJnsjcdizlService;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class JnsjcdizlServiceImpl extends BaseBasicServiceImpl<JnsjcdizlDto, JnsjcdizlModel, IJnsjcdizlDao> implements IJnsjcdizlService {

    @Autowired
    private IJnsjcdizlDao jnsjcdizlDao;

    @Override
    public boolean inserList(List<JnsjcdizlDto> list) {
        return jnsjcdizlDao.insertList(list)>0;
    }
}
