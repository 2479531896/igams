package com.matridx.igams.production.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.SbyjllDto;
import com.matridx.igams.production.dao.entities.SbyjllModel;
import com.matridx.igams.production.dao.entities.SbysDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
public interface ISbyjllService extends BaseBasicService<SbyjllDto, SbyjllModel> {
    List<String> selectNeedScrapList(List<String> list);

    boolean scrapOrSold(SbyjllDto sbyjllDto);

    boolean insertList(List<SbyjllDto> list);

    boolean turnSaveEquipmentAcceptance(SbysDto sbysDto, SbyjllDto sbyjllDto) throws BusinessException;

    /**
     * 钉钉审批回调
     *
     */
    boolean callbackDeviceTurnOverAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;

    /**
     * 设备移交履历的原使用部门和现使用部门
     */
    List<SbyjllDto> selectOldOrNewDepartments(SbyjllDto sbyjllDto);

    /**
     * 设备移交审核列表
     */
    List<SbyjllDto> getPagedAuditDeviceTurnOver(SbyjllDto sbyjllDto);
    /**
     * 批量新增设备移交记录
     */
    void insertSbyjllDtos(List<SbyjllDto> sbyjllDtos);
    /**
     * 设备查看履历
     */
    List<SbyjllDto> getSyjllDtos(SbyjllDto sbyjllDto);
    /**
     * 移交删除
     */
    boolean delTurnOverSave(SbyjllDto sbyjllDto);
}
