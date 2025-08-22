package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;

import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IQualityControlOperationsStaticDao extends BaseBasicDao<SjxxDto, SjxxModel> {
    /**
     * @Description 获取送检样本类型占比
     * @Param [map]
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String, Object>> getSampleTypeMakeUp(Map<String, Object> map);
    /**
     * @Description TODO 
     * @Param [map]
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String, Object>> getInspectionTypeMakeUp(Map<String, Object> map);
    /**
     * @Description TODO 
     * @Param [map]
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String, Object>> getDepartmentTypeMakeUp(Map<String, Object> map);
    /**
     * @Description TODO 
     * @Param [map]
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String, Object>> getHospitalTypeMakeUp(Map<String, Object> map);
    /**
     * @Description TODO 
     * @Param [map]
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String, Object>> getVIPTypeMakeUp(Map<String, Object> map);
    /**
     * @Description TODO
     * @Param [map]
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String, Object>> getTimeoutNumber();
    /**
     * @Description TODO
     * @Param [map]
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String, Object>> getTimeoutNumberGroupByJcdw();
    /**
     * @Description TODO
     * @Param [map]
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String, Object>> getSpeTimeSamples();

    /**
     * 各个实验室饱和度
     * @return
     */
    List<Map<String, Object>> getSaturationJcdw(Map<String, Object> map);

    /**
     * 所有实验室总饱和度
     * @return
     */
    List<Map<String, Object>> getSaturationAllJcdw(Map<String, Object> map);
    /**
     * 每日样本数
     * @return
     */
    List<Map<String, Object>> getDailyybslist();
}
