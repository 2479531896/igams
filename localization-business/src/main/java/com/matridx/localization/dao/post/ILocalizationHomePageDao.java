package com.matridx.localization.dao.post;
import com.matridx.localization.dao.entities.LocalizationStatisticsDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface ILocalizationHomePageDao {
    /*
        获取年龄范围与性别
    */
    List<Map<String, Object>> getAgeRangeAndGender(LocalizationStatisticsDto localizationStatisticsDto);
    /*
        获取样本类型对应数量
    */
    List<Map<String, Object>> getSampleTypeTotal(LocalizationStatisticsDto localizationStatisticsDto);
    /*
        科室分布
    */
    List<Map<String, Object>> getInspectionDepartment(LocalizationStatisticsDto localizationStatisticsDto);
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
    List<Map<String, Object>> getPositiveRate(LocalizationStatisticsDto localizationStatisticsDto);
    /*
        耐药基因检测结果
     */
    List<Map<String, Object>> getDrugResistGeneCount(LocalizationStatisticsDto localizationStatisticsDto);
    /*
        检出微生物信息
     */
    List<Map<String, Object>> getMicrobeInfo(LocalizationStatisticsDto localizationStatisticsDto);
    /*
        样本数、报告数、阳性数
    */
    Map<String, Object> getSampleCountAndReportCountAndPositiveCount(LocalizationStatisticsDto localizationStatisticsDto);
    /*
        样本数、报告数、阳性数(不限制伙伴、单位)
    */
    Map<String, Object> getSampleCountAndReportCountAndPositiveCountMapWhole(LocalizationStatisticsDto localizationStatisticsDto);
    /*
      年龄段的检出微生物占比
    */
    List<Map<String,Object>> getMicrobeInfoForAgeRange(LocalizationStatisticsDto localizationStatisticsDto);
    /*
      检出这个微生物的样本类型的分布
    */
    List<Map<String, Object>> getSampleTypeTotalForMicrobe(LocalizationStatisticsDto localizationStatisticsDto);
    /*
      指定样本类型的检出微生物检出趋势
     */
    List<Map<String, Object>> microbeTrendForSampleType(LocalizationStatisticsDto localizationStatisticsDto);
    /*
        各个年龄段的指定耐药基因的检出趋势
     */
    List<Map<String, Object>> ageRangeTrendForDrugResistGene(LocalizationStatisticsDto localizationStatisticsDto);
    /*
       指定样本类型的所有检出微生物
     */
    List<Map<String, Object>> allMicrobeForSampleType(LocalizationStatisticsDto clone);
}
