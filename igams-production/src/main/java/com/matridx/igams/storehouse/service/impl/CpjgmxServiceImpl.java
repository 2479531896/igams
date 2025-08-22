package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.CpjgmxDto;
import com.matridx.igams.storehouse.dao.entities.CpjgmxModel;
import com.matridx.igams.storehouse.dao.post.ICpjgmxDao;
import com.matridx.igams.storehouse.service.svcinterface.ICpjgmxService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CpjgmxServiceImpl extends BaseBasicServiceImpl<CpjgmxDto, CpjgmxModel, ICpjgmxDao> implements ICpjgmxService {

    @Override
    public Boolean batchInsertDtoList(List<CpjgmxDto> cpjgmxDtos) {
        return dao.batchInsertDtoList(cpjgmxDtos)!=0;
    }

    @Override
    public Boolean updateList(List<CpjgmxDto> cpjgmxDtos) {
        return dao.updateList(cpjgmxDtos)!=0;
    }
}
