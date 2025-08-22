package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.SbtkDto;
import com.matridx.igams.storehouse.dao.entities.SbtkModel;

import java.util.List;

public interface ISbtkService extends BaseBasicService<SbtkDto, SbtkModel>{
    /**
     * @description 获取所有使用部门
     */
    List<SbtkDto> getDepartmentList();
    /**
     * @description 获取所有管理人员
     */
    List<SbtkDto> getGlryList();
    /**
     * @description 退库申请审核列表
     * @param sbtkDto
     * @return
     */
    List<SbtkDto> getPagedAuditStockreturnDevice(SbtkDto sbtkDto);
    /**
     * @description 设备退库删除
     * @param sbtkDto
     * @return boolean
     */
    boolean delStockreturnDevice(SbtkDto sbtkDto) throws BusinessException;
}
