package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.dao.entities.XszbDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author WYX
 * @version 1.0
 * @className SjxxTwoService
 * @description TODO
 * @date 17:33 2023/5/10
 **/
public interface ISjxxTwoService {
    /**
     * 全国趋势
     */
    List<Map<String,String>> getAllCountryChanges(SjxxDto sjxxDto);
    /**
     * 产品趋势
     */
    List<Map<String,Object>> getProductionChanges(SjxxDto sjxxDto);
	   /**
     * @description 根据伙伴分类和平台获取第三方Top20伙伴
     * @param sjxxDto
     * @return
     */
    List<Map<String, String>> getTopDsf20(SjxxDto sjxxDto);
    /**
     * @description 根据伙伴分类和平台获取直销Top20伙伴
     * @param sjxxDto
     * @return
     */
    List<Map<String, String>> getTopZx20(SjxxDto sjxxDto);
    /**
     * @description 根据伙伴分类和平台获取CSOTop20伙伴
     * @param sjxxDto
     * @return
     */
    List<Map<String, String>> getTopCSO20(SjxxDto sjxxDto);
    /**
     * @description 获取入院信息
     * @param sjxxDto
     * @return
     */
    List<Map<String, String>> getTopRY20(SjxxDto sjxxDto);
	  /**
     * 平台趋势
     */
    List<Map<String,Object>> getPlatformChanges(SjxxDto sjxxDto);

    /**
     * 模糊查询内部编码
     * @param nbbm
     * @return
     */
    List<String> getSjxxByLikeNbbm(String nbbm);
    /**
     * 平台业务占比
     */
    List<Map<String,Object>> getPlatformProportion(XszbDto xszbDto);
    /**
     * 平台业务占比
     */
    List<Map<String,Object>> getProductionProportion(XszbDto xszbDto);
	    /**
     * 收费测试数 科室分类(直销+代理)
     * @param sjxxDto
     * @return
     */
    List<Map<String, Object>> getChargesDivideByKs(SjxxDto sjxxDto);
    /**
     * 收费测试数 样本分类(直销+代理)
     * @param sjxxDto
     * @return
     */
    List<Map<String, Object>> getChargesDivideByYblx(SjxxDto sjxxDto);

    /**
     * 核心医院的收费检测数(先展示测试数前10的）
     * @param sjxxDto
     * @return
     */
    List<Map<String, Object>> getHxyyTopList(SjxxDto sjxxDto);
    /**
     * @description 销售端平台趋势
     * @param sjxxDto
     * @return
     */
    List<Map<String, String>> getAllCountryChangesForSale(SjxxDto sjxxDto);

    /**
     * 定时更新redis营销周报统计数据
     */
    void weekLeadStatisDefault();

    /**
     * 获取申请单信息
     * @param request
     * @return
     */
    Map<String, Object> getApplicationDetail(HttpServletRequest request);

    /**
     * 获取样本报告
     * @param request
     * @param barcode
     * @param jcxmcsdm
     * @return
     */
    Map<String,Object> sendSampleReport(HttpServletRequest request, String barcode, String jcxmcsdm);

    List<Map<String,Object>> getDateSjxxList(String dateStr);
}
