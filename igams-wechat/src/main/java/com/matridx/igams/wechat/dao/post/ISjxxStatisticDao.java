package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.XszbDto;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxjgModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISjxxStatisticDao extends BaseBasicDao<SjxxDto, SjxxjgModel>
{
	
//  微信发送日报 周报 统计
	
	/**
	 * 微信统计按照标本个数进行统计
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxDaily(SjxxDto sjxxDto);

	/**
	 * 微信统计按照标本个数进行统计(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxDailyByJsrq(SjxxDto sjxxDto);

	/**
	 * 微信统计按照合作伙伴统计标本标本个数
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxBySjhbDaily(SjxxDto sjxxDto);

	/**
	 * 微信统计按照合作伙伴统计标本标本个数(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxBySjhbDailyAndJsrq(SjxxDto sjxxDto);

	/**
	 *  微信统计统计复检信息
	 * @param fjsqDto
	 * @return
	 */
    List<Map<String,String>> getFjsqDaily(FjsqDto fjsqDto);
	
	/**
	 * 微信统计废弃标本
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getFqybByYbztDaily(SjxxDto sjxxDto);
	
	/**
	 * 微信统计报告阳性率
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getJyjgDaily(SjxxDto sjxxDto);
	
	/**
	 * 微信统计送检清单
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getSjxxListDaily(SjxxDto sjxxDto);

	/**
	 * 微信统计送检清单(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getSjxxListDailyByJsrq(SjxxDto sjxxDto);

	/**
	 * 微信统计检测项目次数
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getJcxmSumDaily(SjxxDto sjxxDto);

	/**
	 * 微信统计检测项目次数(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getJcxmSumDailyByJsrq(SjxxDto sjxxDto);

	/**
	 * 微信统计收费项目检测项目条数
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getJcxmInSfsfDaily(SjxxDto sjxxDto);

	/**
	 * 微信统计收费项目检测项目条数(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getJcxmInSfsfDailyByJsrq(SjxxDto sjxxDto);

	/**
	 * 微信统计标本数信息（周报）
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxByWeekly(SjxxDto sjxxDto);
	/**
	 * 微信统计标本数信息（周报）(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxByWeeklyAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 微信统计标本数信息————按照周统计（周报）
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getybxx_weekByWeekly(SjxxDto sjxxDto);

	/**
	 * 微信统计标本数信息————按照周统计（周报）(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getybxx_weekByWeeklyAndJsrq(SjxxDto sjxxDto);

	/**
	 * 
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSfsfByWeekly(SjxxDto sjxxDto);

	/**
	 *
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSfsfByWeeklyAndJsrq(SjxxDto sjxxDto);

	/**
	 * 微信统计测试数信息————D+R的算两个（周）
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxDRByWeekly(SjxxDto sjxxDto);
	/**
	 * 微信统计测试数信息————D+R的算两个（周）(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxDRByWeeklyAndJsrq(SjxxDto sjxxDto);

	/**
	 * 微信周统计测试数信息
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxDR_weeek(SjxxDto sjxxDto);

	/**
	 * 微信周统计测试数信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxDR_weeekByJsrq(SjxxDto sjxxDto);

	/**
	 * 微信统计收费标本的测试数信息（周）
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxTypeByWeekly(SjxxDto sjxxDto);
	/**
	 * 微信统计收费标本的测试数信息（周）(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxTypeByWeeklyAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 微信按照周统计收费标本的测试数信息
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxType_week(SjxxDto sjxxDto);

	/**
	 * 微信按照周统计收费标本的测试数信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxType_weekByJsrq(SjxxDto sjxxDto);

	/**
	 * 微信统计上上周的临床反馈结果
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getLcfkOfBeforeWeekly(SjxxDto sjxxDto);
	
	/**
	 * 微信统计标本前三的阳性率（周）
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getJyjgWeekly(SjxxDto sjxxDto);
	
	/**
	 * 微信统计送检科室（周）
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getKsByWeekly(SjxxDto sjxxDto);

	/**
	 * 微信统计送检科室（周）(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getKsByWeeklyAndJsrq(SjxxDto sjxxDto);

	/**
	 * 微信统计合作医疗机构
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getCooperativeWeekly(SjxxDto sjxxDto);

	/**
	 * 微信统计合作医疗机构(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getCooperativeWeeklyByJSrq(SjxxDto sjxxDto);

	/**
	 * 按照周对合作伙伴进行统计
	 * @return
	 */
    List<Map<String, String>> getSjhbByWeekly(SjxxDto sjxxDto);

	/**
	 * 按照周对合作伙伴进行统计(接收日期)
	 * @return
	 */
    List<Map<String, String>> getSjhbByWeeklyAndJsrq(SjxxDto sjxxDto);

	/**
	 * 微信统计标本个数
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>>  getYblxByJsrq(SjxxDto sjxxDto);

	/**
	 * 微信统计标本个数(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>>  getYblxByJsrqAndJsrq(SjxxDto sjxxDto);

	/**
	 * 微信统计报告数量（周）
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjbgByBgrq(SjxxDto sjxxDto);
	
	/**
	 * 微信统计每天标本的阳性率（周）
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getJyjgBybgrq(SjxxDto sjxxDto);
	
	/**
	 * 微信按照周统计标本个数（周）
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getYbxxnumByWeekly(SjxxDto sjxxDto);

	/**
	 * 微信按照周统计标本个数（周）(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getYbxxnumByWeeklyAndJsrq(SjxxDto sjxxDto);

	/**
	 * 微信统计
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getJyjgLxByWeekly(SjxxDto sjxxDto);
	
	/**
	 * 微信周报按照日期和代表名统计报告数量
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjhbFlByWeekly(SjxxDto sjxxDto);

	/**
	 * 微信周报按照日期和代表名统计报告数量(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjhbFlByWeeklyAndJSrq(SjxxDto sjxxDto);

	/**
	 * 根据代表名按周查询信息
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjhbFl_week(SjxxDto sjxxDto);

	/**
	 * 根据代表名按周查询信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjhbFl_weekByJsrq(SjxxDto sjxxDto);

	/**
	 * 根据统计名称名查询信息
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getWeeklyByTjFl(SjxxDto sjxxDto);
	/**
	 * 根据统计名称名查询信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getWeeklyByTjFlAndJsrq(SjxxDto sjxxDto);

	/**
	 * 根据统计名称名查询信息,用报表名称筛选
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getWeeklyByTjFlTm(SjxxDto sjxxDto);

	/**
	 * 根据统计名称名查询信息,用报表名称筛选(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getWeeklyByTjFlTmAndJsrq(SjxxDto sjxxDto);

	/**
	 * 根据统计名称按周查询信息
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getWeeklyByTjFl_week(SjxxDto sjxxDto);

	/**
	 * 根据统计名称按周查询信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getWeeklyByTjFl_weekAndJsrq(SjxxDto sjxxDto);

	/**
	 * 领导周报，销售各分类的检测量(本sql区别getStatisByFl ，主要是为了减少数据量)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByFlLimit(SjxxDto sjxxDto);
	/**
	 * 领导周报，销售各分类的检测量(本sql区别getStatisByFl ，主要是为了减少数据量)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByFlLimitAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 领导周报，按周统计销售各分类的检测量(本sql区别getStatisByFl ，主要是为了减少数据量)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByWeekFlLimit(SjxxDto sjxxDto);
	/**
	 * 领导周报，按周统计销售各分类的检测量(本sql区别getStatisByFl ，主要是为了减少数据量)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByWeekFlLimitAndJsrq(SjxxDto sjxxDto);

	/**
	 * 领导周报，各省份的检测量数据统计(统计一周)，用于显示在省份名称的旁边)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSfStatisByWeek(SjxxDto sjxxDto);
	/**
	 * 领导周报，各省份的检测量数据统计(统计一周)，用于显示在省份名称的旁边)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSfStatisByWeekAndJsrq(SjxxDto sjxxDto);

	/**
	 * 查询检测单位分组
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,Object>> getSjxxByJcdw(SjxxDto sjxxDto);
	
	/**
	 * 根据检测单位查询统计数据
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,Object>> getSjxxInJcdw(SjxxDto sjxxDto);
	
	/**
	 * 通过省份查询送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,Object>> getSjxxBySfPie(SjxxDto sjxxDto);
		
	/**
	 * 查询未上机检测单位分组
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,Object>> getWsjByJcdw(SjxxDto sjxxDto);
	
	/**
	 * 根据检测单位查询未上机统计数据
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,Object>> getSjxxInJcdwByWsj(SjxxDto sjxxDto);
	
	/**
	 *根据检测单位查询未上机列表数据
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getSjxxInJcdwByWsjList(SjxxDto sjxxDto);

	/**
	 * 查询接收后但未实验的标本信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getNotSyListDaily(SjxxDto sjxxDto);

	/**
	 * 查询接收后但未实验的标本信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getNotSyListDailyByJsrq(SjxxDto sjxxDto);

	/**
	 * 销售达成率统计
	 * 注意：如果按月查询，需要传递 bgrqstart 和 bgrqend,按年查询需要传递 bgrqYstart 与 bgrqYend，不可同时传递两个
	 * 需传销售指标类型，用于关联查询销售指标。
	 * @param xszbDto
	 * @return
	 */
    List<XszbDto> getSalesAttainmentRate(XszbDto xszbDto);
	/**
	 * 销售达成率统计(接收日期)
	 * 注意：如果按月查询，需要传递 bgrqstart 和 bgrqend,按年查询需要传递 bgrqYstart 与 bgrqYend，不可同时传递两个
	 * 需传销售指标类型，用于关联查询销售指标。
	 * @param xszbDto
	 * @return
	 */
    List<XszbDto> getSalesAttainmentRateByJsrq(XszbDto xszbDto);

	/**
	 * 查询区域下的销售达成率
	 * @param xszbDto
	 * @return
	 */
    List<Map<String, String>> getSalesAttainmentRateByArea(XszbDto xszbDto);
	/**
	 * 查询区域下的销售达成率(接收日期)
	 * @param xszbDto
	 * @return
	 */
    List<Map<String, String>> getSalesAttainmentRateByAreaAndJsrq(XszbDto xszbDto);

    /**
	 * 查询指定时间内的伙伴测试数
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisBySomeTimeDB(SjxxDto sjxxDto);
    /**
	 * 查询指定时间内的伙伴测试数(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisBySomeTimeDBAndJsrq(SjxxDto sjxxDto);

	/**
	 * 统计标本质量合格率
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getQualifiedRateByRq(Map<String,Object> map);
	/**
	 * 统计各标本类型的不合格率
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getDisqualificationRateWithTypeByRq(Map<String,Object> map);
	/**
	 * 收费测试数 样本分类(直销+代理)
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getNumberOfChargesDivideByYblx(Map<String,Object> map);
	/**
	 * 收费测试数 科室分类(直销+代理)
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getNumberOfChargesDivideByKs(Map<String,Object> map);
	/**
	 * 获取销售目标达成率
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getKpiList(Map<String,Object> map);

	/**
	 * 渠道收费测试数
	 * @param map
	 * @return
	 */
    List<Map<String, Object>> getSfChannelChargeData(Map<String,Object> map);

	/**
	 * 业务类型收费测试数
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getSfBusinessTypeChargeData(Map<String,Object> map);
	/**
	 * 渠道收费免费测试数
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getSfMfChannelChargeData(Map<String,Object> map);

	/**
	 * 业务类型收费免费测试数
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getSfMfBusinessTypeChargeData(Map<String,Object> map);
	/**
	 * 核心医院收费占比
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getCentralHospitalData(Map<String,Object> map);
	/**
	 * 核心医院销售额占比
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getCentralHospitalSaleData(Map<String,Object> map);
/**
	 * 获取质控医院信息数据
	 */
	List<Map<String,Object>> getHospitalSales(Map<String,Object> map);
	/**
	 * 获取质控医院销售额信息数据
	 */
	List<Map<String,Object>> getHospitalSalesNum(Map<String,Object> map);

	/**
	 *获得直销
	 * @param map
	 * @return
	 */

	List<Map<String, Object>> getZx(Map<String,Object> map);
	/**
	 * 获取丢失客户信息
	 */
	List<Map<String, Object>> getLosePartners(Map<String,Object> map);
	/**
	 * 入院医院的收费检测数(先展示测试数前10的）
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getRyyySfcssTopList(Map<String, Object> map);
	/**
	 * 入院医院的收费销售额(先展示测试数前10的）
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getRyyySfcssSaleTopList(Map<String, Object> map);

	/**
	 * 区域收费测试数（各大区展示[直销+代理商]）饼状图（数字+比例）
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getQySfcssList(Map<String, Object> map);

	List<Map<String, Object>> getNdyrjSfcssList(Map<String, Object> map);

    /**
	 * 获得新增的伙伴
	 */
	List<Map<String,Object>> getNewPartner(Map<String,Object> map);
	/**
	 * 获得新增的伙伴
	 */
	List<Map<String,Object>> getNewPartnertotal(Map<String,Object> map);
	/**
	 * @Description 第三方实验室TOP10排行
	 * @Param [map]
	 * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 */
	List<Map<String, Object>>  getTopDsf(Map<String, Object> map);
	/**
	 * @Description 增加代理商(代理+CSO)top10统计图
	 * @Param [map]
	 * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 */
	List<Map<String, Object>> getTopDls(Map<String, Object> map);

	List<Map<String, Object>> getTjNdyrjSfcssList(Map<String, Object> map);

	List<Map<String, Object>> getRyNdyrjSfcssList(Map<String, Object> map);
	/**
	 * @description 获取渠道收费销售金额
	 * @param map
	 * @return
	 */
    List<Map<String, Object>> getSfChannelChargeDataWithAmount(Map<String, Object> map);
	/**
	 * @description 获取业务类型收费销售金额
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getSfBusinessTypeChargeDataWithAmount(Map<String, Object> map);

	List<Map<String, Object>> getZxWithAmount(Map<String, Object> map);

	List<Map<String, Object>> getTopDsfWithAmount(Map<String, Object> map);

	List<Map<String, Object>> getTopDlsWithAmount(Map<String, Object> map);

	/**
	 * 月人均收费销售额
	 * @param map
	 * @return
	 */
    List<Map<String, Object>> getNdyrjSfxseList(Map<String, Object> map);

	/**
	 * 特检月人均收费销售额
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getTjNdyrjSfxseList(Map<String, Object> map);

	/**
	 * 入院月人均收费销售额
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getRyNdyrjSfxseList(Map<String, Object> map);

	/**
	 * 获取平台销售指标达成率
	 * @param xszbDto
	 * @return
	 */
	List<Map<String, Object>> getPtzbdcl(XszbDto xszbDto);
	/**
	 * 获取业务发展部
	 * @param xszbDto
	 * @return
	 */
	List<Map<String, Object>> getYwfzbZbdcl(XszbDto xszbDto);
	/**
	* 获取销售统计平台达成率
	*/
	List<Map<String, String>> getTjsxdcl(XszbDto xszbDto);
	/**
	* 获取伙伴分类测试数统计
	*/
	List<Map<String, String>> getHbflTestCountStatistics(SjxxDto sjxxDto);

	/**
	 * 获取合作伙伴统计
	 */
	List<Map<String, String>> getStatisticsByTjFlAndJsrq(SjxxDto sjxxDto);

	/**
	 * 获取合作伙伴测试数占比
	 */
	List<Map<String, String>> getHbflcsszb(SjxxDto sjxxDto);
	/**
	 * 获取送检区分测试数占比
	 */
	List<Map<String, String>> getSjqfcsszb(SjxxDto sjxxDto);
}
