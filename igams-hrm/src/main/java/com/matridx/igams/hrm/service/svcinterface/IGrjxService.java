package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.GrjxDto;
import com.matridx.igams.hrm.dao.entities.GrjxModel;
import com.matridx.igams.hrm.dao.entities.JxmbDto;
import com.matridx.igams.hrm.dao.entities.YghmcDto;

import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
public interface IGrjxService extends BaseBasicService<GrjxDto, GrjxModel> {
    /**
     * @description 发放绩效
     */
    boolean distributePerformance(JxmbDto jxmbDto) throws Exception;
    /**
     * 获取最近的一个绩效
     */
    GrjxDto getLastPerformance(GrjxDto grjxDto);
    /**
     * 获取待办数据
     */
    List<GrjxDto> getToDoList(GrjxDto grjxDto);
    /**
     * 审核列表数据（不分页）
     */
    List<GrjxDto> getAuditListData(GrjxDto grjxDto);
    /**
     * @description 废弃个人绩效
     */
    boolean discardPerformance(GrjxDto grjxDto) throws BusinessException;
    /**
     * @description 修改保存个人绩效
     */
    boolean modSavePerformance(GrjxDto grjxDto) throws BusinessException;
    /**
     * 获取年份
     */
    List<String> getGrjxYears();
    /**
     * 获取部门
     */
    List<Map<String,String>> getGrjxBms();
    /**
     * 确认绩效
     */
    boolean confirmPerformance(GrjxDto gxjxDto);
    /**
     * @description 绩效确认列表
     */
    List<GrjxDto> getPagedListPerformanceConfirm(GrjxDto grjxDto);
    /**
     * @description 发放撤回
     */
    boolean releaseRecallPerformance(GrjxDto grjxDto) throws BusinessException;
    /**
     * @description 批量发放绩效
     */
    boolean batchDistributePerformance(JxmbDto jxmbDto) throws Exception;
    /**
     * @description 获取绩效统计数据
     */
    Map<String,Object> getPerformanceStatistics(YghmcDto yghmcDto);
    /**
     * @description 获取绩效统计数据详情
     */
    List<Map<String, Object>> getPerformanceStatisticsView(YghmcDto yghmcDto);

    boolean delByIds(GrjxDto grjxDto)throws BusinessException;

    boolean checkDistributePerformance(JxmbDto jxmbDto) throws BusinessException;
}
