package com.matridx.igams.hrm.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.JqxzDto;
import com.matridx.igams.hrm.dao.entities.JqxzModel;
import com.matridx.igams.hrm.dao.post.IJqxzDao;
import com.matridx.igams.hrm.service.svcinterface.IJqxzService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:JYK
 */
@Service
public class JqxzServiceImpl extends BaseBasicServiceImpl<JqxzDto, JqxzModel, IJqxzDao> implements IJqxzService {
    @Override
    public boolean insertList(List<JqxzDto> list) {
        return dao.insertList(list);
    }
}
