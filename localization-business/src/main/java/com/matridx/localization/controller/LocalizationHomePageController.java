package com.matridx.localization.controller;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.localization.dao.entities.LocalizationStatisticsDto;
import com.matridx.localization.service.svcinterface.ILocalizationHomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/localizationHomePage")
public class LocalizationHomePageController extends BaseController {
    @Autowired
    ILocalizationHomePageService localizationHomePageService;
    @RequestMapping("/localizationHomePage/getLocalizationHomePageStatistics")
    @ResponseBody
    public Map<String,Object> getLocalizationHomePageStatistics(LocalizationStatisticsDto localizationStatisticsDto){
        localizationStatisticsDto.setInit(true);
        localizationStatisticsDto.setCalendar(Calendar.getInstance());
        Map<String,Object> result = new HashMap<>();
        try {
            //年龄与性别分布
            List<Map<String,Object>> ageRangeAndGender = localizationHomePageService.getAgeRangeAndGender(localizationStatisticsDto);
            result.put("ageRangeAndGender",ageRangeAndGender);
            //样本类型分布
            List<Map<String,Object>> sampleTypeTotal = localizationHomePageService.getSampleTypeTotal(localizationStatisticsDto);
            result.put("sampleTypeTotal",sampleTypeTotal);
            //科室分布
            List<Map<String,Object>> inspectionDepartment = localizationHomePageService.getInspectionDepartment(localizationStatisticsDto);
            result.put("inspectionDepartment",inspectionDepartment);
            //微生物较上周变化情况
            List<Map<String,Object>> microbeChange = localizationHomePageService.getMicrobeChange(localizationStatisticsDto);
            result.put("microbeChange",microbeChange);
            //本周样本数变化
            List<Map<String,Object>> sampleCountChange = localizationHomePageService.getSampleCountChange(localizationStatisticsDto);
            result.put("sampleCountChange", sampleCountChange);
            //送检报告阳性率
            List<Map<String,Object>> positiveRate = localizationHomePageService.getPositiveRate(localizationStatisticsDto);
            result.put("positiveRate", positiveRate);
            //耐药基因检测结果
            List<Map<String,Object>> drugResistGeneCount = localizationHomePageService.getDrugResistGeneCount(localizationStatisticsDto);
            result.put("drugResistGeneCount", drugResistGeneCount);
            //检出微生物信息
            List<Map<String,Object>> microbeInfo = localizationHomePageService.getMicrobeInfo(localizationStatisticsDto);
            result.put("microbeInfo", microbeInfo);
            Map<String,Object> sampleCountAndReportCountAndPositiveCount =  localizationHomePageService.getSampleCountAndReportCountAndPositiveCount(localizationStatisticsDto);
            result.put("sampleCountAndReportCountAndPositiveCount", sampleCountAndReportCountAndPositiveCount);
        } catch (BusinessException e) {
            result.put("errorMsg",e.getMsg());
        }
        return result;
    }
    @RequestMapping("/localizationHomePage/getLocalizationHomePageStatisticsForParams")
    @ResponseBody
    public Map<String,Object> getLocalizationHomePageStatisticsForParams(LocalizationStatisticsDto localizationStatisticsDto){
        localizationStatisticsDto.setCalendar(Calendar.getInstance());
        Map<String,Object> result = new HashMap<>();
        try {
            switch (localizationStatisticsDto.getSqlKey()){
                case "ageRangeAndGender":
                    //年龄与性别分布
                    List<Map<String,Object>> ageRangeAndGender = localizationHomePageService.getAgeRangeAndGender(localizationStatisticsDto);
                    result.put("ageRangeAndGender",ageRangeAndGender);
                    break;
                case "sampleTypeTotal":
                    //样本类型分布
                    List<Map<String,Object>> sampleTypeTotal = localizationHomePageService.getSampleTypeTotal(localizationStatisticsDto);
                    result.put("sampleTypeTotal",sampleTypeTotal);
                    break;
                case "inspectionDepartment":
                    //科室分布
                    List<Map<String,Object>> inspectionDepartment = localizationHomePageService.getInspectionDepartment(localizationStatisticsDto);
                    result.put("inspectionDepartment",inspectionDepartment);
                    break;
                case "microbeChange":
                    //微生物较上周变化情况
                    List<Map<String,Object>> microbeChange = localizationHomePageService.getMicrobeChange(localizationStatisticsDto);
                    result.put("microbeChange",microbeChange);
                    break;
                case "sampleCountChange":
                    //本周样本数变化
                    List<Map<String,Object>> sampleCountChange = localizationHomePageService.getSampleCountChange(localizationStatisticsDto);
                    result.put("sampleCountChange", sampleCountChange);
                    break;
                case "positiveRate":
                    //送检报告阳性率
                    List<Map<String,Object>> positiveRate = localizationHomePageService.getPositiveRate(localizationStatisticsDto);
                    result.put("positiveRate", positiveRate);
                    break;
                case "drugResistGeneCount":
                    //耐药基因检测结果
                    List<Map<String,Object>> drugResistGeneCount = localizationHomePageService.getDrugResistGeneCount(localizationStatisticsDto);
                    result.put("drugResistGeneCount", drugResistGeneCount);
                    break;
                case "microbeInfo":
                    //检出微生物信息
                    List<Map<String,Object>> microbeInfo = localizationHomePageService.getMicrobeInfo(localizationStatisticsDto);
                    result.put("microbeInfo", microbeInfo);
                    break;
                case "sampleCountAndReportCountAndPositiveCount":
                    //样本数、报告数、阳性数
                    Map<String,Object> sampleCountAndReportCountAndPositiveCount =  localizationHomePageService.getSampleCountAndReportCountAndPositiveCount(localizationStatisticsDto);
                    result.put("sampleCountAndReportCountAndPositiveCount", sampleCountAndReportCountAndPositiveCount);
                    break;
                case "microbeInfoForAgeRange":
                    //年龄段的检出微生物占比
                    List<Map<String,Object>> microbeInfoForAgeRange =  localizationHomePageService.getMicrobeInfoForAgeRange(localizationStatisticsDto);
                    result.put("microbeInfoForAgeRange", microbeInfoForAgeRange);
                    break;
                case "sampleTypeTotalForMicrobe":
                    //检出这个微生物的样本类型的分布
                    List<Map<String,Object>> sampleTypeTotalForMicrobe =  localizationHomePageService.getSampleTypeTotalForMicrobe(localizationStatisticsDto);
                    result.put("sampleTypeTotalForMicrobe", sampleTypeTotalForMicrobe);
                    break;
                case "microbeTrendForSampleType":
                    //指定样本类型的检出微生物检出趋势
                    List<Map<String,Object>> microbeTrendForSampleType =  localizationHomePageService.microbeTrendForSampleType(localizationStatisticsDto);
                    result.put("microbeTrendForSampleType", microbeTrendForSampleType);
                    break;
                case "ageRangeTrendForDrugResistGene":
                    //各个年龄段的指定耐药基因的检出趋势
                    List<Map<String,Object>> ageRangeTrendForDrugResistGene =  localizationHomePageService.ageRangeTrendForDrugResistGene(localizationStatisticsDto);
                    result.put("ageRangeTrendForDrugResistGene", ageRangeTrendForDrugResistGene);
                default:
                    break;
            }
        }catch (BusinessException e){
            result.put("errorMsg",e.getMsg());
        }
        return result;
    }
    @RequestMapping("/localizationHomePage/getLocalizationHomePageStatisticsMapWhole")
    @ResponseBody
    public Map<String,Object> getLocalizationHomePageStatisticsMapWhole(LocalizationStatisticsDto localizationStatisticsDto){
        localizationStatisticsDto.setInit(true);
        localizationStatisticsDto.setCalendar(Calendar.getInstance());
        Map<String,Object> result = new HashMap<>();
        try {
            //年龄与性别分布
            List<Map<String,Object>> ageRangeAndGender = localizationHomePageService.getAgeRangeAndGender(localizationStatisticsDto);
            result.put("ageRangeAndGender",ageRangeAndGender);
            //样本类型分布
            List<Map<String,Object>> sampleTypeTotal = localizationHomePageService.getSampleTypeTotal(localizationStatisticsDto);
            result.put("sampleTypeTotal",sampleTypeTotal);
            //科室分布
            List<Map<String,Object>> inspectionDepartment = localizationHomePageService.getInspectionDepartment(localizationStatisticsDto);
            result.put("inspectionDepartment",inspectionDepartment);
            //微生物较上周变化情况
            List<Map<String,Object>> microbeChange = localizationHomePageService.getMicrobeChange(localizationStatisticsDto);
            result.put("microbeChange",microbeChange);
            //本周样本数变化
            List<Map<String,Object>> sampleCountChange = localizationHomePageService.getSampleCountChange(localizationStatisticsDto);
            result.put("sampleCountChange", sampleCountChange);
            //送检报告阳性率
            List<Map<String,Object>> positiveRate = localizationHomePageService.getPositiveRate(localizationStatisticsDto);
            result.put("positiveRate", positiveRate);
            //耐药基因检测结果
            List<Map<String,Object>> drugResistGeneCount = localizationHomePageService.getDrugResistGeneCount(localizationStatisticsDto);
            result.put("drugResistGeneCount", drugResistGeneCount);
            //检出微生物信息
            List<Map<String,Object>> microbeInfo = localizationHomePageService.getMicrobeInfo(localizationStatisticsDto);
            result.put("microbeInfo", microbeInfo);
            Map<String,Object> sampleCountAndReportCountAndPositiveCount =  localizationHomePageService.getSampleCountAndReportCountAndPositiveCountMapWhole(localizationStatisticsDto);
            result.put("sampleCountAndReportCountAndPositiveCount", sampleCountAndReportCountAndPositiveCount);
        } catch (BusinessException e) {
            result.put("errorMsg",e.getMsg());
        }
        return result;
    }
    @RequestMapping("/localizationHomePage/getLocalizationHomePageStatisticsForParamsMapWhole")
    @ResponseBody
    public Map<String,Object> getLocalizationHomePageStatisticsForParamsMapWhole(LocalizationStatisticsDto localizationStatisticsDto){
        localizationStatisticsDto.setCalendar(Calendar.getInstance());
        Map<String,Object> result = new HashMap<>();
        try {
            switch (localizationStatisticsDto.getSqlKey()){
                case "ageRangeAndGender":
                    //年龄与性别分布
                    List<Map<String,Object>> ageRangeAndGender = localizationHomePageService.getAgeRangeAndGender(localizationStatisticsDto);
                    result.put("ageRangeAndGender",ageRangeAndGender);
                    break;
                case "sampleTypeTotal":
                    //样本类型分布
                    List<Map<String,Object>> sampleTypeTotal = localizationHomePageService.getSampleTypeTotal(localizationStatisticsDto);
                    result.put("sampleTypeTotal",sampleTypeTotal);
                    break;
                case "inspectionDepartment":
                    //科室分布
                    List<Map<String,Object>> inspectionDepartment = localizationHomePageService.getInspectionDepartment(localizationStatisticsDto);
                    result.put("inspectionDepartment",inspectionDepartment);
                    break;
                case "microbeChange":
                    //微生物较上周变化情况
                    List<Map<String,Object>> microbeChange = localizationHomePageService.getMicrobeChange(localizationStatisticsDto);
                    result.put("microbeChange",microbeChange);
                    break;
                case "sampleCountChange":
                    //本周样本数变化
                    List<Map<String,Object>> sampleCountChange = localizationHomePageService.getSampleCountChange(localizationStatisticsDto);
                    result.put("sampleCountChange", sampleCountChange);
                    break;
                case "positiveRate":
                    //送检报告阳性率
                    List<Map<String,Object>> positiveRate = localizationHomePageService.getPositiveRate(localizationStatisticsDto);
                    result.put("positiveRate", positiveRate);
                    break;
                case "drugResistGeneCount":
                    //耐药基因检测结果
                    List<Map<String,Object>> drugResistGeneCount = localizationHomePageService.getDrugResistGeneCount(localizationStatisticsDto);
                    result.put("drugResistGeneCount", drugResistGeneCount);
                    break;
                case "microbeInfo":
                    //检出微生物信息
                    List<Map<String,Object>> microbeInfo = localizationHomePageService.getMicrobeInfo(localizationStatisticsDto);
                    result.put("microbeInfo", microbeInfo);
                    break;
                case "sampleCountAndReportCountAndPositiveCount":
                    //样本数、报告数、阳性数
                    Map<String,Object> sampleCountAndReportCountAndPositiveCount =  localizationHomePageService.getSampleCountAndReportCountAndPositiveCountMapWhole(localizationStatisticsDto);
                    result.put("sampleCountAndReportCountAndPositiveCount", sampleCountAndReportCountAndPositiveCount);
                    break;
                case "microbeInfoForAgeRange":
                    //年龄段的检出微生物占比
                    List<Map<String,Object>> microbeInfoForAgeRange =  localizationHomePageService.getMicrobeInfoForAgeRange(localizationStatisticsDto);
                    result.put("microbeInfoForAgeRange", microbeInfoForAgeRange);
                    break;
                case "sampleTypeTotalForMicrobe":
                    //检出这个微生物的样本类型的分布
                    List<Map<String,Object>> sampleTypeTotalForMicrobe =  localizationHomePageService.getSampleTypeTotalForMicrobe(localizationStatisticsDto);
                    result.put("sampleTypeTotalForMicrobe", sampleTypeTotalForMicrobe);
                    break;
                case "microbeTrendForSampleType":
                    //指定样本类型的检出微生物检出趋势
                    List<Map<String,Object>> microbeTrendForSampleType =  localizationHomePageService.microbeTrendForSampleType(localizationStatisticsDto);
                    result.put("microbeTrendForSampleType", microbeTrendForSampleType);
                    break;
                case "ageRangeTrendForDrugResistGene":
                    //各个年龄段的指定耐药基因的检出趋势
                    List<Map<String,Object>> ageRangeTrendForDrugResistGene =  localizationHomePageService.ageRangeTrendForDrugResistGene(localizationStatisticsDto);
                    result.put("ageRangeTrendForDrugResistGene", ageRangeTrendForDrugResistGene);
                default:
                    break;
            }
        }catch (BusinessException e){
            result.put("errorMsg",e.getMsg());
        }
        return result;
    }
}
