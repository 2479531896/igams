package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.GlwjxxDto;
import com.matridx.igams.production.dao.entities.GlwjxxModel;
import com.matridx.igams.production.dao.post.IGlwjxxDao;
import com.matridx.igams.production.service.svcinterface.IGlwjxxService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlwjxxServiceImpl extends BaseBasicServiceImpl<GlwjxxDto, GlwjxxModel, IGlwjxxDao> implements IGlwjxxService {

    @Override
    public void insertGlwjxxDtos(List<GlwjxxDto> glwjxxDtos) {
        dao.insertGlwjxxDtos(glwjxxDtos);
    }
}
