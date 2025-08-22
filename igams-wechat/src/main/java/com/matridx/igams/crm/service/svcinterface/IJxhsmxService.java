package com.matridx.igams.crm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.crm.dao.entities.JxhsmxDto;
import com.matridx.igams.crm.dao.entities.JxhsmxModel;

import java.util.List;

public interface IJxhsmxService extends BaseBasicService<JxhsmxDto, JxhsmxModel>{

    /**
     * 批量新增绩效核算明细
     * @param jxhsmxDtos
     * @return
     */
    boolean insertDtos(List<JxhsmxDto> jxhsmxDtos);
    /**
     * 批量删除绩效核算明细
     * @param jxhsmxDtos
     * @return
     */
    boolean deleteDtos(List<JxhsmxDto> jxhsmxDtos);
    /**
     * 批量更新绩效核算明细
     * @param jxhsmxDtos
     * @return
     */
    boolean updateDtos(List<JxhsmxDto> jxhsmxDtos);
}
