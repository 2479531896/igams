package com.matridx.crf.web.service.impl;

import com.matridx.crf.web.dao.entities.JnsjjcjgDto;
import com.matridx.crf.web.dao.entities.JnsjjcjgModel;
import com.matridx.crf.web.dao.post.IJnsjjcjgDao;
import com.matridx.crf.web.service.svcinterface.IJnsjjcjgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.matridx.igams.common.service.BaseBasicServiceImpl;

import java.util.List;


@Service
public class JnsjjcjgServiceImpl extends BaseBasicServiceImpl<JnsjjcjgDto, JnsjjcjgModel, IJnsjjcjgDao> implements IJnsjjcjgService {

    @Autowired
    private IJnsjjcjgDao jnsjjcjgDao;

    @Override
    public boolean insertList(List<JnsjjcjgDto> list) {
        return jnsjjcjgDao.insertList(list)>0;
    }

}
