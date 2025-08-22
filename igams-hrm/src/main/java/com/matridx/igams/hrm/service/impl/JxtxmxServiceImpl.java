package com.matridx.igams.hrm.service.impl;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.JxtxmxDto;
import com.matridx.igams.hrm.dao.entities.JxtxmxModel;
import com.matridx.igams.hrm.dao.post.IJxtxmxDao;
import com.matridx.igams.hrm.service.svcinterface.IJxtxmxService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WYX
 * @version 1.0
 * @className JxtxmxServiceImpl
 * @description TODO
 **/
@Service
public class JxtxmxServiceImpl extends BaseBasicServiceImpl<JxtxmxDto, JxtxmxModel, IJxtxmxDao> implements IJxtxmxService {

    @Override
    public boolean insertJxtxmxDtos(List<JxtxmxDto> jxtxmxDtos) {
        return dao.insertJxtxmxDtos(jxtxmxDtos);
    }

    @Override
    public boolean updateJxtxmxDtos(List<JxtxmxDto> jxtxmxDtos) {
        return dao.updateJxtxmxDtos(jxtxmxDtos);
    }

    @Override
    public boolean deleteByJxtxid(JxtxmxDto jxtxmxDto) {
        return dao.deleteByJxtxid(jxtxmxDto);
    }

}
