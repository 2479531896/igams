package com.matridx.igams.web.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.web.dao.entities.WbcxDto;
import com.matridx.igams.web.dao.entities.WbcxModel;
import com.matridx.igams.web.dao.post.IWbcxDao;
import com.matridx.igams.web.service.svcinterface.IWbcxService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WbcxServiceImpl extends BaseBasicServiceImpl<WbcxDto, WbcxModel, IWbcxDao> implements IWbcxService {

    @Override
    public List<WbcxDto> getSsgsList() {
        return dao.getSsgsList();
    }
}
