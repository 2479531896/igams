package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.YsmxDto;
import com.matridx.igams.production.dao.entities.YsmxModel;
import com.matridx.igams.production.dao.post.IYsmxDao;
import com.matridx.igams.production.service.svcinterface.IYsmxService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YsmxServiceImpl extends BaseBasicServiceImpl<YsmxDto, YsmxModel, IYsmxDao> implements IYsmxService {


    @Override
    public boolean insertList(List<YsmxDto> list) {
        return dao.insertList(list);
    }

    @Override
    public boolean delObsoleteData(YsmxDto ysmxDto) {
        return dao.delObsoleteData(ysmxDto);
    }
}
