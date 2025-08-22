package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.JxmbDto;
import com.matridx.igams.hrm.dao.entities.JxmbModel;
import com.matridx.igams.hrm.dao.entities.YghmcDto;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
public interface IJxmbService extends BaseBasicService<JxmbDto, JxmbModel> {
    /**
     * 新增保存绩效模板数据
     */
    boolean addSaveTemplate(JxmbDto jxmbDto) throws BusinessException;
    /**
     * 修改保存绩效模板数据
     */
    boolean modSaveTemplate(JxmbDto jxmbDto) throws BusinessException;
    /**
     * 获取待办数据
     */
    List<JxmbDto> getToDoList(JxmbDto jxmbDto);
    /**
     * 审核列表数据（不分页）
     */
    List<JxmbDto> getAuditListData(JxmbDto jxmbDto);

    /**
     * 重写删除方法
     */
    boolean delete(JxmbDto jxmbDto) throws BusinessException;
    /**
     * 获取绩效模板的所有部门
     */
    List<JxmbDto> getAllMbJgxx(JxmbDto jxmbDto);
    /**
     * @description 获取在指定日期创建模板人员
     */
    List<JxmbDto> getCreatByRq(List<JxmbDto> jxmbDtos);
    /**
     * @description 获取绩效目标统计数据
     */
    Map<String,Object> getPerformanceTargetStatistics(YghmcDto yghmcDto);
    /**
     * @description 获取绩效目标统计数据详情
     */
    List<Map<String, Object>> getPerformanceTargetStatisticsView(YghmcDto yghmcDto);
}
