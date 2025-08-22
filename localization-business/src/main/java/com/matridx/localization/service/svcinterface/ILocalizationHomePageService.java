package com.matridx.localization.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.localization.dao.entities.LocalizationStatisticsDto;

import java.util.List;
import java.util.Map;

public interface ILocalizationHomePageService {
    /*
        获取年龄范围与性别
     */
    List<Map<String, Object>> getAgeRangeAndGender(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException;
    /*
        获取样本类型对应数量
     */
    List<Map<String, Object>> getSampleTypeTotal(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException;
    /*
        科室分布
     */
    List<Map<String, Object>> getInspectionDepartment(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException;
    /*
        微生物较上周变化情况
     */
    List<Map<String, Object>> getMicrobeChange(LocalizationStatisticsDto localizationStatisticsDto);
    /*
        本周样本数变化
     */
    List<Map<String, Object>> getSampleCountChange(LocalizationStatisticsDto localizationStatisticsDto);
    /*
        报告阳性率
     */
    List<Map<String, Object>> getPositiveRate(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException;
    /*
        耐药基因检测结果数量
     */
    List<Map<String, Object>> getDrugResistGeneCount(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException;
    /*
        检出微生物信息
     */
    List<Map<String, Object>> getMicrobeInfo(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException;
    /*
        样本数、报告数、阳性数
     */
    Map<String, Object> getSampleCountAndReportCountAndPositiveCount(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException;
    /*
        样本数、报告数、阳性数(不限制伙伴、单位)
     */
    Map<String, Object> getSampleCountAndReportCountAndPositiveCountMapWhole(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException;
    /*
       年龄段的检出微生物占比
     */
    List<Map<String,Object>> getMicrobeInfoForAgeRange(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException;
    /*
      检出这个微生物的样本类型的分布
    */
    List<Map<String, Object>> getSampleTypeTotalForMicrobe(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException;
    /*
      指定样本类型的检出微生物检出趋势
     */
    List<Map<String, Object>> microbeTrendForSampleType(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException;
    /*
        耐药基因的各个年龄段的检出趋势
     */
    List<Map<String, Object>> ageRangeTrendForDrugResistGene(LocalizationStatisticsDto localizationStatisticsDto) throws BusinessException;

    void dealLocalHospitalInspect(String str) ;

    /**
     * @Description: 发送消息
     * @param str
     * @return void
     * @Author: 郭祥杰
     * @Date: 2025/4/27 14:42
     */
    void sendMessage(String str) ;
}
