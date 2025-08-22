package com.matridx.igams.hrm.service.svcinterface;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.JxtxDto;
import com.matridx.igams.hrm.dao.entities.JxtxModel;

public interface IJxtxService extends BaseBasicService<JxtxDto, JxtxModel> {
    /**
     * @description 新增保存绩效提醒信息
     */
    boolean addSavePerformanceReminder(JxtxDto jxtxDto) throws BusinessException;
    /**
     * @description 修改保存绩效提醒信息
     */
    boolean modSavePerformanceReminder(JxtxDto jxtxDto) throws BusinessException;
    /**
     * @description 删除绩效提醒信息
     */
    boolean delPerformanceReminder(JxtxDto jxtxDto) throws BusinessException;
}
