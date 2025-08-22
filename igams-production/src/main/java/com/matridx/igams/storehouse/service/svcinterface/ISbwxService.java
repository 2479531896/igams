package com.matridx.igams.storehouse.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.SbwxDto;
import com.matridx.igams.storehouse.dao.entities.SbwxModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ISbwxService extends BaseBasicService<SbwxDto, SbwxModel>{
    /**
     * @description 维修保养设备审批回调
     * @param json_obj request t_map
     * @return boolean
     */
    boolean callbackUpkeepDeviceAduit(JSONObject json_obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;
    /**
     * @description 删除维修保养数据
     * @param sbwxDto
     * @return boolean
     */
    boolean delUpkeepDevice(SbwxDto sbwxDto)throws BusinessException;
    /**
     * @description 获取维修/保养审核列表
     * @param sbwxDto
     * @return
     */
    List<SbwxDto> getPagedAuditUpkeepDevice(SbwxDto sbwxDto);
    /**
     * @description 获取所有使用部门
     */
    List<SbwxDto> getDepartmentList();
    /**
     * @description 获取所有管理人员
     */
    List<SbwxDto> getGlryList();
    /**
     * @description 获取流水号
     * @param prefix
     * @return
     */
    String getSbwxSerial(String prefix);
}
