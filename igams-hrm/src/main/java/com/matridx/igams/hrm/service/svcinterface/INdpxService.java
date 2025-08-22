package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.NdpxDto;
import com.matridx.igams.hrm.dao.entities.NdpxModel;

import java.util.List;

public interface INdpxService extends BaseBasicService<NdpxDto, NdpxModel>{

    /**
     * 获取筛选数据
     */
    List<String> getFilterData();
    /**
     * 获取工作管理数据
     */
    List<NdpxDto> getTaskInfo(NdpxDto ndpxDto);
    /**
     * 新增保存
     */
    boolean addSaveAnnualPlan(NdpxDto ndpxDto);
    /**
     * 修改保存
     */
    boolean modSaveAnnualPlan(NdpxDto ndpxDto);
    /**
     * 删除
     */
    boolean delAnnualPlan(NdpxDto ndpxDto);

}
