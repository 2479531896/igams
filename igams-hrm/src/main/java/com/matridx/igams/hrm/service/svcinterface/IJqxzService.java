package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.JqxzDto;
import com.matridx.igams.hrm.dao.entities.JqxzModel;

import java.util.List;

/**
 * @author:JYK
 */
public interface IJqxzService extends BaseBasicService<JqxzDto, JqxzModel> {
    boolean insertList(List<JqxzDto> list);
}
