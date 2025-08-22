package com.matridx.igams.production.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.SbbfDto;
import com.matridx.igams.production.dao.entities.SbbfModel;
import com.matridx.igams.production.dao.entities.SbysDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
public interface ISbbfService extends BaseBasicService<SbbfDto, SbbfModel> {
    /**
     * 设备报废保存
     */
    boolean scrapSaveEquipmentAcceptance(SbysDto sbysDto,SbbfDto sbbfDto) throws BusinessException;
    /**
     * 钉钉审批回调
     *
     */
    boolean callbackDeviceScarpAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;
    /**
     * 获取部门
     */
    List<SbbfDto> getDepartmentList();
    /**
     * 获取管理人员
     */
    List<SbbfDto> getLeadList();

    /**
     * 删除报废列表
     */
    boolean delDeviceScarp(SbbfDto sbbfDto,String flag);
    /**
     * 设备报废列表
     */
    List<SbbfDto> getPagedAuditDeviceScrap(SbbfDto sbbfDto);
}
