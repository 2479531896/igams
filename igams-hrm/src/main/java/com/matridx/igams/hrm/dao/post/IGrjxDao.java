package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.GrjxDto;
import com.matridx.igams.hrm.dao.entities.GrjxModel;
import com.matridx.igams.hrm.dao.entities.YghmcDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
@Mapper
public interface IGrjxDao extends BaseBasicDao<GrjxDto, GrjxModel> {
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(GrjxDto grjxDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<GrjxDto> getListForSearchExp(GrjxDto grjxDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<GrjxDto> getListForSelectExp(GrjxDto grjxDto);
    /**
     * @description 批量插入个人绩效信息
     */
    boolean insertGrjxDtos(List<GrjxDto> grjxDtos);
    /**
     * 获取最近的一个绩效
     */
    GrjxDto getLastPerformance(GrjxDto grjxDto);
	/**
     * 获取需要自动提交的个人绩效
     */
    List<GrjxDto> getNeedSubmitByMbid(GrjxDto grjxDto);
    /**
     * 获取待办数据
     */
    List<GrjxDto> getToDoList(GrjxDto grjxDto);

    /**
     * 获取审核列表
     */
    List<GrjxDto> getPagedAuditData(GrjxDto grjxDto);
    /**
     * 审核列表
     */
    List<GrjxDto> getAuditList(List<GrjxDto> list);
    /**
     * @description 批量修改个人绩效明细信息
     */
    boolean updateGrjxDtos(List<GrjxDto> grjxDtos);
    /**
     * 获取年份
     */
    List<String> getGrjxYears();
    /**
     * 获取部门
     */
    List<Map<String,String>> getGrjxBms();
    /**
     * @description 绩效确认列表
     */
    List<GrjxDto> getPagedListPerformanceConfirm(GrjxDto grjxDto);
    /**
     * @description 通过绩效id获取绩效消息
     */
    List<GrjxDto> getDtoListByIds(GrjxDto grjxDto);
    /**
     * @description 通过yhid获取绩效消息
     */
    List<GrjxDto> getDtoListByYhIds(GrjxDto grjxDto);
    /**
     * @description 获取绩效统计数据
     */
    Map<String,Object> getPerformanceStatistics(YghmcDto yghmcDto);

    /**
     * @description 获取绩效统计数据详情
     */
    List<Map<String, Object>> getPerformanceStatisticsView(YghmcDto yghmcDto);
    /**
     * @description 通过模板设置id获取需要提醒的绩效
     */
    List<GrjxDto> getNeedRemindByMbid(GrjxDto grjxDto);
}
