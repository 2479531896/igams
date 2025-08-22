package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.JxmbDto;
import com.matridx.igams.hrm.dao.entities.JxmbModel;
import com.matridx.igams.hrm.dao.entities.YghmcDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
@Mapper
public interface IJxmbDao extends BaseBasicDao<JxmbDto, JxmbModel> {
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(JxmbDto jxmbDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<JxmbDto> getListForSearchExp(JxmbDto jxmbDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<JxmbDto> getListForSelectExp(JxmbDto jxmbDto);
    /**
     * 获取待办数据
     */
    List<JxmbDto> getToDoList(JxmbDto jxmbDto);

    /**
     * 获取审核列表
     */
    List<JxmbDto> getPagedAuditData(JxmbDto jxmbDto);
    /**
     * 审核列表
     */
    List<JxmbDto> getAuditList(List<JxmbDto> list);
    /**
     * 绩效模板列表页面修改
     */
    boolean  updatePageEvent(JxmbDto jxmbDto);
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
