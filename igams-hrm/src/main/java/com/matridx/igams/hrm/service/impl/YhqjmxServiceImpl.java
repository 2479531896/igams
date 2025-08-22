package com.matridx.igams.hrm.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.YhqjmxDto;
import com.matridx.igams.hrm.dao.entities.YhqjmxModel;
import com.matridx.igams.hrm.dao.post.IYhqjmxDao;
import com.matridx.igams.hrm.service.svcinterface.IYhqjmxService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:JYK
 */
@Service
public class YhqjmxServiceImpl extends BaseBasicServiceImpl<YhqjmxDto, YhqjmxModel, IYhqjmxDao> implements IYhqjmxService {
    @Override
    public boolean updateList(List<YhqjmxDto> list) {
        return dao.updateList(list);
    }

    @Override
    public void insertList(List<YhqjmxDto> list) {
        dao.insertList(list);
    }
}
