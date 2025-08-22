package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.JxtxmxDto;
import com.matridx.igams.hrm.dao.entities.JxtxmxModel;

import java.util.List;

public interface IJxtxmxService extends BaseBasicService<JxtxmxDto, JxtxmxModel> {
    /**
     * @description 新增绩效提醒明细信息
     */
    boolean insertJxtxmxDtos(List<JxtxmxDto> jxtxmxDtos);
    /**
     * @description 修改绩效提醒明细信息
     */
    boolean updateJxtxmxDtos(List<JxtxmxDto> jxtxmxDtos);
    /**
     * @description 删除绩效提醒明细信息
     */
    boolean deleteByJxtxid(JxtxmxDto jxtxmxDto);
}
