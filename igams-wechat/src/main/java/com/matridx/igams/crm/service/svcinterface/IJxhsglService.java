package com.matridx.igams.crm.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.crm.dao.entities.JxhsglDto;
import com.matridx.igams.crm.dao.entities.JxhsglModel;
import com.matridx.igams.wechat.dao.entities.SwyszkDto;

import java.util.List;
import java.util.Map;

public interface IJxhsglService extends BaseBasicService<JxhsglDto, JxhsglModel>{
    /**
     * 编辑保存绩效核算管理
     * @param jxhsglDto
     * @return
     */
    Map<String,Object> editSaveJxhsgl(JxhsglDto jxhsglDto, User user) throws BusinessException;

    /**
     * 删除保存绩效核算管理
     * @param jxhsglDto
     * @return
     */
    Map<String, Object> delSaveJxhsgl(JxhsglDto jxhsglDto, User user) throws BusinessException;

    /**
     * 编辑保存设置信息
     * @param data
     * @return
     */
    Map<String, Object> editSaveSettings(Map<String, Object> data, User user) throws BusinessException;

    /**
     * 审核列表
     * @param jxhsglDto
     * @return
     */
    List<JxhsglDto> getPagedAuditList(JxhsglDto jxhsglDto);

    boolean savePerformance(List<SwyszkDto> list,User user,JxhsglDto jxhsglDto);
}
