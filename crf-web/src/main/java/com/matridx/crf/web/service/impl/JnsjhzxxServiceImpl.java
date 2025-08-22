package com.matridx.crf.web.service.impl;

import com.matridx.crf.web.dao.entities.JnsjhzxxDto;
import com.matridx.crf.web.dao.entities.JnsjhzxxModel;
import com.matridx.crf.web.dao.post.IJnsjhzxxDao;
import com.matridx.crf.web.service.svcinterface.IJnsjhzxxService;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class JnsjhzxxServiceImpl extends BaseBasicServiceImpl<JnsjhzxxDto, JnsjhzxxModel, IJnsjhzxxDao> implements IJnsjhzxxService {

    @Override
    public List<JcsjDto> getHospitailList(JnsjhzxxDto jnsjhzxxDto) {
        return dao.getHospitailList(jnsjhzxxDto);
    }
}
