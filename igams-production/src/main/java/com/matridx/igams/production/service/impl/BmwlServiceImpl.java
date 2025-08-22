package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.BmwlDto;
import com.matridx.igams.production.dao.entities.BmwlModel;
import com.matridx.igams.production.dao.post.IBmwlDao;
import com.matridx.igams.production.service.svcinterface.IBmwlService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BmwlServiceImpl extends BaseBasicServiceImpl<BmwlDto, BmwlModel, IBmwlDao> implements IBmwlService {

    @Override
    public List<BmwlDto> getAllBm() {
        return dao.getAllBm();
    }
}
