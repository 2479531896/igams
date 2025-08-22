package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxModel;
import com.matridx.igams.wechat.dao.entities.SjybztDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface ISjxxDao extends BaseBasicDao<SjxxDto, SjxxModel>{

	/**
	* 查询科室信息
	* @return
	*/
    List<SjdwxxDto> getSjdw();
     
 	/**
 	 * 根据接口条件确认记录条数
 	 * @param sjxxDto
 	 * @return
 	 */
    int getInfListCount(SjxxDto sjxxDto);
 	
 	/**
 	 * 根据接口条件查找记录
 	 * @param sjxxDto
 	 * @return
 	 */
    List<SjxxDto> getInfList(SjxxDto sjxxDto);

	/**
	 * 查找未收款记录
	 * @return
	 */
    List<SjxxDto> getDtoBySf(String csid);
	/**
	 * 分页查找未收款记录
	 * @return
	 */
    List<SjxxDto> getPagedDtoBySf(SjxxDto sjxxDto);
	/**
	 * 查找未收款记录
	 * @return
	 */
    List<SjxxDto> getCountByYxdb(SjxxDto sjxxDto);
	/**
	 * 查找未收款记录发送给医学代表
	 * @return
	 */
    List<SjxxDto> getListByYxdb(SjxxDto sjxxDto);
	/**
	 * 查找未收款记录发送给大区经理
	 * @return
	 */
    List<SjxxDto> getListByDqjl(SjxxDto sjxxDto);
	/**
	 * 查找未收款记录数发送给大区经理
	 * @return
	 */
    List<SjxxDto> getCountByDqjl(SjxxDto sjxxDto);


	
	/**
	 * 根据送检ID查询附件表信息
	 * @param sjid
	 * @return
	 */
    List<FjcfbDto> selectFjByWjid(String sjid);
	
	/**
	 * 修改合作伙伴
	 * @param sjxxDto
	 * @return
	 */
    boolean updateDB(SjxxDto sjxxDto);
	
	/**
	 * 根据搜索条件获取导出条数
	 * @param sjxxDto
	 * @return
	 */
    int getCountForSearchExp(SjxxDto sjxxDto);

	/**
	 * 选中导出物料数据
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListForSearchExp(SjxxDto sjxxDto);
	/**
	 * 从数据库分页获取导出送检信息数据
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListForSelectExp(SjxxDto sjxxDto);
	/**
	 * 更新收样确认信息
	 * @param sjxxDto
	 * @return
	 */
    int updateConfirmInfo(SjxxDto sjxxDto);

	/**
	 * 根据送检id查询物种信息
	 * @param sjxxDto
	 * @return
	 */
	 List<SjwzxxDto> selectWzxxBySjid(SjxxDto sjxxDto);


	/**
	 * 更新报告日期
	 * @param sjxxDto
	 * @return
	 */
    int updateReport(SjxxDto sjxxDto);
	
	/**
	 * 图片预览
	 * @param sjxxDto
	 * @return
	 */
    List<FjcfbDto> preview(SjxxDto sjxxDto);

	/**
	 * 根据代表名查询接收报告结果人员列表
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getReceiveUserByDb(SjxxDto sjxxDto);
	
	/**
	 * 同步修改信息
	 * @param sjxxDto
	 * @return
	 */
    boolean updateforbioinformation(SjxxDto sjxxDto);
	

	/**
	 * 根据日期查询实验的记录数据
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> selectInspSize(SjxxDto sjxxDto);
	
		/**
	 * 通过送检id查询检测标记
	 * @param sjxxDto
	 * @return
	 */
        SjxxDto selectjcbjByid(SjxxDto sjxxDto);
	
	/**
	 * 修改检测标记
	 * @param sjxxDto
	 * @return
	 */
	boolean updateJcbjByid(SjxxDto sjxxDto);
	
	//统计信息
	/**
	 * 根据标本类型统计标本信息
	 * @return
	 */
	List<Map<String, String>> getStatisByYblx(SjxxDto sjxxDto);
	/**
	 * 根据标本类型统计标本信息(接收日期)
	 * @return
	 */
	List<Map<String, String>> getStatisByYblxAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 按照日期统计标本个数，可按照日，月，年，显示指定前几个,需传递起始时间和时间列表
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getStatisYblxByJsrq(SjxxDto sjxxDto);
	/**
	 * 按照日期统计标本个数，可按照日，月，年，显示指定前几个,需传递起始时间和时间列表(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getStatisYblxByJsrqAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 按照日期统计送检报告个数，可按照日，月，年，显示指定前几个,需传递起始时间和时间列表
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getStatisSjbgByBgrq(SjxxDto sjxxDto);
	
	/**
	 * 按照合作伙伴进行统计
	 * @return
	 */
	List<Map<String, String>> getStatisBysjhb(SjxxDto sjxxDto);
	/**
	 * 按照合作伙伴进行统计(接收日期)
	 * @return
	 */
	List<Map<String, String>> getStatisBysjhbAndJsrq(SjxxDto sjxxDto);
	/**
	 * 按照省份检索检出物种前10个
	 * @return
	 */
	List<Map<String, String>> getStatisWzSf(SjxxDto sjxxDto);
	/**
	 * 按照周对合作伙伴进行统计
	 * @return
	 */
	List<Map<String, String>> getStatisWeekBysjhb(SjxxDto sjxxDto);
	/**
	 * 按照周对合作伙伴进行统计(接收日期)
	 * @return
	 */
	List<Map<String, String>> getStatisWeekBysjhbAndJsrq(SjxxDto sjxxDto);
	
	/**
	 *按照合作伙伴，查找所属的送检单位的送检信息
	 * @return
	 */
	List<Map<String, String>> getSjdwBydb(SjxxDto sjxxDto);
	/**
	 * 高关注度
	 * @return
	 */
	List<Map<String, String>> getWzxxByGgzd(SjxxDto sjxxDto);
	/**
	 * 根据送检医生统计标本
	 * @return
	 */
	List<Map<String, Object>> getYblxBySjys(SjxxDto sjxxDto);

	/**
	 * 修改送检信息
	 * @param sjxxDto
	 * @return
	 */
    int updateModel(SjxxDto sjxxDto);
	
	/**
	 * 统计结果类型为高关，疑似以及无的标本数量
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getYbnumByJglx(SjxxDto sjxxDto);
	
	/**
	 * 统计每天标本的阳性率
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getStatisYxlBybgrq(SjxxDto sjxxDto);
	
	/**
	 * 按照每周统计标本
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getStatisWeekYbsByJsrq(SjxxDto sjxxDto);
	/**
	 * 按照每周统计标本(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getStatisWeekYbsByJsrqAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 查询当天标本数量
	 * @param sjxxDto
	 * @return
	 */
	Map<String,Object> getYbslByday(SjxxDto sjxxDto);

	
	/**
	 * 查询前一天收到的标本并且今天已经产生报告的标本数量
	 * @param sjxxDto
	 * @return
	 */
	Map<String,Object> getybwcd(SjxxDto sjxxDto);
	
	/**
	 * 获取最近一周标本送检路线
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String,Object>> getSjxlxx(SjxxDto sjxxDto);
	
	/**
	 * 查询当天录入标本数
	 * @param sjxxDto
	 * @return
	 */
	Map<String,Object> getLrYbslByday(SjxxDto sjxxDto);
	
	/**
	 * 根据收费情况进行统计收样(按照不收费的标本变化，收费标本的变化)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjxxBySy(SjxxDto sjxxDto);

	/**
	 * 根据收费情况进行统计收样(按照不收费的标本变化，收费标本的变化)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjxxBySyAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 根据收费情况以周为单位进行统计收样(按照不收费的标本变化，收费标本的变化)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjxxWeekBySy(SjxxDto sjxxDto);
	
	/**
	 * 根据收费情况以周为单位进行统计收样(按照不收费的标本变化，收费标本的变化)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjxxWeekBySyAndJsrq(SjxxDto sjxxDto);

	/**
	 * 日报统计
	 * @return
	 */
    List<Map<String, String>> getYbxxByDay(SjxxDto sjxxDto);


	/**
	 * 日报统计(接收日期)
	 * @return
	 */
    List<Map<String, String>> getYbxxByDayAndJsrq(SjxxDto sjxxDto);

	/**
	 * 获取到所有的合作伙伴
	 * @return
	 */
    List<String> getDb(String jsrq);
	
	/**
	 * 获取到所有的统计名称
	 * @return
	 */
    List<String> getTjmc(SjxxDto sjxxDto);

	/**
	 * 获取到所有的统计名称(接收日期)
	 * @return
	 */
    List<String> getTjmcByJsrq(SjxxDto sjxxDto);

	/**
	 * 按照合作伙伴分类统计
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getYbxxByDb(SjxxDto sjxxDto);
	
	/**
	 * 按照统计名称分类统计
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getYbxxByTjmc(SjxxDto sjxxDto);
	/**
	 * 按照统计名称分类统计(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getYbxxByTjmcAndJsrq(SjxxDto sjxxDto);
	
	/**
	 *统计复检申请
	 * @param fjsqDto
	 * @return
	 */
    List<Map<String,String>> getFjsqByDay(FjsqDto fjsqDto);
	
	/**
	 * 统计废弃标本
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getFqybByYbzt(SjxxDto sjxxDto);
	
	/**
	 * 修改参数扩展
	 * @param sjxxDto
	 * @return
	 */
    boolean updateCskz(SjxxDto sjxxDto);
	
	/**
	 * 查询扩展参数
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getSjxxCskz(SjxxDto sjxxDto);
	
	/**
	 * 根据 患者姓名，合作伙伴的信息，去查找非本标本编号的其他同类信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getPagedTlxxList(SjxxDto sjxxDto);
	
	/**
	 * 根据送检ids查询送检物种信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjwzxxDto> selectWzxxBySjids(SjxxDto sjxxDto);
	
	/**
	 * 根据ids查询送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> selectSjxxBySjids(SjxxDto sjxxDto);

	
	/**
	 * 根据 患者姓名，合作伙伴的信息，去查找非本标本编号的其他同类信息(不分页)
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getTlxxList(SjxxDto sjxxDto);
	
	/**
	 * 查询内部编码是不是已经存在
	 * @param sjxxDto
	 * @return
	 */
    Integer getCountBynbbm(SjxxDto sjxxDto);

	/**
	 * 根据代表名查询信息
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByFl(SjxxDto sjxxDto);
	
	/**
	 * 根据统计名称名查询信息
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByTjFl(SjxxDto sjxxDto);
	
	/**
	 * 根据统计名称名查询信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByTjFlAndJsrq(SjxxDto sjxxDto);

	/**
	 * 根据代表名按周查询信息
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByWeekFl(SjxxDto sjxxDto);
	
	/**
	 * 根据统计名称按周查询信息
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByWeekTjFl(SjxxDto sjxxDto);
		
	/**
	 * 根据统计名称按周查询信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByWeekTjFlAndJsrq(SjxxDto sjxxDto);

	/**
	 * 查询标本编号是不是已经存在
	 * @param sjxxDto
	 * @return
	 */
    Integer getCountByybbh(SjxxDto sjxxDto);
	/**
	 * 验证标本编号以及外部编码是不是已经存在
	 * @return
	 */
    Integer getCountByWbbmAndYbbh(SjxxDto sjxxDto);
	
	/**
	 * 生信部访问查询方法
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getDtoForbiont(SjxxDto sjxxDto);
	
	/**
	 * 生信部修改操作
	 * @param SjxxDto
	 * @return
	 */
    boolean updateForbiont(SjxxDto SjxxDto);

	/**
	 * 根据合作伙伴查询送检数量
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByDb(SjxxDto sjxxDto);
	
	/**
	 * 根据统计名称查询送检数量
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByTj(SjxxDto sjxxDto);
	/**
	 * 根据统计名称查询送检数量(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByTjAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 废弃标本列表
	 * @param sjybztDto
	 * @return
	 */
    List<SjxxDto> getPageFqybList(SjybztDto sjybztDto);
	
	/**
	 * 统计所有的溶血标本（nbbm为B开头的）
	 * @param sjybztDto
	 * @return
	 */
    Integer getCountAllSjxx(SjybztDto sjybztDto);
	/**
	 * 统计所有的溶血标本（nbbm为B开头的）(接收日期)
	 * @param sjybztDto
	 * @return
	 */
    Integer getCountAllSjxxByJsrq(SjybztDto sjybztDto);
	
	/**
	 * 统计所有状态为溶血的标本数量
	 * @param sjybztDto
	 * @return
	 */
    Integer getCountByzt(SjybztDto sjybztDto);
	
	/**
	 * 统计所有的废弃标本
	 * @param sjybztDto
	 * @return
	 */
    Integer getAllFqyb(SjybztDto sjybztDto);
	
	/**
	 * 根据送检ids判断所选数据检测项目是否相同
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> checkJcxm(SjxxDto sjxxDto);
	
	/**
	 * 统计今日实验数
	 * @param sjxxDto
	 * @return
	 */
    int getJcbjBySyrq(SjxxDto sjxxDto);
	
	/**
	 * 判断当天实验的标本实验报告是否全部完成
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> checkSybgSfwc(SjxxDto sjxxDto);
	
	/**
	 * 获取前一天实验的标本已出报告的数量
	 * @param sjxxDto
	 * @return
	 */
    Map<String,String> getYwcbgs(SjxxDto sjxxDto);

	/**
	 * 获取发送情况清单
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String,String>> getFsbgs(SjxxDto sjxxDto);

	/**
	 * 	获取时间段
	 * @param sjxxDto
	 * @return
	 */
	List<String> getTimeByTime(SjxxDto sjxxDto);
	
	/**
	 * 数据滚动刷新
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListSjxx(SjxxDto sjxxDto);
	
	/**
	 * 查询刷新之间新增的送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListSjxxBylrsj(SjxxDto sjxxDto);
	
	/**
	 * 报告阳性率列表
	 * @param sjwzxxDto
	 * @return
	 */
    List<SjxxDto> getListPositive(SjwzxxDto sjwzxxDto);
	
	/**
	 * 合作伙伴查看送检列表
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getPageDtoBySjhb(SjxxDto sjxxDto);
	
	/**
	 * 统计送检物种信息
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getWzxxBySjid(SjxxDto sjxxDto);
	
	/**
	 * 添加送检反馈
	 * @param sjxxDto
	 * @return
	 */
    boolean updateFeedBack(SjxxDto sjxxDto);
	
	/**
	 * 自定义统计（统计图显示）
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getCustomStatis(SjxxDto sjxxDto);
	
	/**
	 * 自定义统计（列表显示）
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getCustomStatisList(SjxxDto sjxxDto);
	
	/**
	 * 自定义统计（统计图显示）获取统计字段的所有信息，统一统计标题
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getCustomStatisHead(SjxxDto sjxxDto);
	

	/**
	 * 自定义统计（统计图显示）统计标题获取统计信息
	 * @param param
	 * @return
	 */
    List<Map<String,String>> getCustomStatisByHead(Map<String, Object> param);

	/**
	 * 根据送检ID查询伙伴信息
	 * @param sjid
	 * @return
	 */
    List<SjxxDto> selectBgmbBySjid(String sjid);
	
	/**
	 * 日报增加送检清单
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListBydaily(SjxxDto sjxxDto);
	/**
	 * 日报增加送检清单(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListBydailyAndJsrq(SjxxDto sjxxDto);
	/**
	 * 查询组装内部编码所需要的的数据 
	 * @param ybbh
	 * @return
	 */
    SjxxDto getconfirmDm(String ybbh);
	
	/**
	 * 生成流水号规则
	 * @param sjxxDto
	 * @return
	 */
    String getSerial(SjxxDto sjxxDto);

	/**
	 * 查询组装内部编码所需要的的数据
	 * @param ybbh
	 * @return
	 */
	List<Map<String,String>> getconfirmDmInfo(String ybbh);

	/**
	 * 生成自定义流水号
	 * @param map
	 * @return
	 */
	String getCustomSerial(Map<String,Object> map);

	/**
	 * 检测项目个数
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxDRByDay(SjxxDto sjxxDto);

	/**
	 * 检测项目个数(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxDRByDayAndJsrq(SjxxDto sjxxDto);

	/**
	 * 检测项目个数(按照检测单位进行)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxDRByDayAndJcdw(SjxxDto sjxxDto);

	/**
	 * 检测项目个数(按照检测单位进行)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxDRByDayAndJcdwAndJsrq(SjxxDto sjxxDto);

	/**
	 * 检测项目个数(按照一段周期内检测单位进行)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjxxcssBySomeTimeAndJcdw(SjxxDto sjxxDto);
	/**
	 * 检测项目个数(按照一段周期内检测单位进行)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjxxcssBySomeTimeAndJcdwAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 收费标本里边检测数量
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxTypeByDay(SjxxDto sjxxDto);

	/**
	 * 收费标本里边检测数量(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxTypeByDayAndJsrq(SjxxDto sjxxDto);

	/**
	 * 按照日期统计收费标本
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>>  getSjxxDRBySy(SjxxDto sjxxDto);
	/**
	 * 按照日期统计收费标本(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>>  getSjxxDRBySyAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 按照周统计检测总条数
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjxxWeekDRBySy(SjxxDto sjxxDto);
	
	/**
	 * 按照周统计检测总条数(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjxxWeekDRBySyAndJsrq(SjxxDto sjxxDto);

	/**
	 * 周报统计收费标本下边检测项目的总条数
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxTypeBYWeek(SjxxDto sjxxDto);
	/**
	 * 周报统计收费标本下边检测项目的总条数(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxTypeBYWeekAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 周报统计收费标本下边的检测项目总条数(通过周)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getWeekYbxxType(SjxxDto sjxxDto);
	/**
	 * 周报统计收费标本下边的检测项目总条数(通过周)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getWeekYbxxTypeByJsrq(SjxxDto sjxxDto);
	
	/**
	 * 统计上上周的临床反馈结果
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getLcfkByBefore(SjxxDto sjxxDto);

	/**
	 * 根据条件查询需要下载的报告信息
	 * @param map
	 * @return
	 */
    List<SjxxDto> selectDownloadReport(Map<String, Object> map);
	
	/**
	 * 根据样类型统计前三的阳性率 
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getJyjgByYblx(SjxxDto sjxxDto);
	
	/**
	 * 增加科室的圆饼统计图
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getKsByweek(SjxxDto sjxxDto);
	/**
	 * 增加科室的圆饼统计图(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getKsByweekAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 增加合作医院数，科室数，医生数的统计表
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjdwSjysKs(SjxxDto sjxxDto);
	/**
	 * 增加合作医院数，科室数，医生数的统计表(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjdwSjysKsByJsrq(SjxxDto sjxxDto);
	
	
	/**
	 * 用一条sql 查询送检信息的其他信息
	 * @param sjid
	 * @return
	 */
    Map<String,String> getAllSjxxOther(String sjid);

	/**
	 * 微信端同步修改方法
	 * @param sjxxDto
	 * @return
	 */
    int weChatUpdate(SjxxDto sjxxDto);

	/**
	 * 根据选择查询报告信息(pdf结果)
	 * @param map
	 * @return
	 */
    List<SjxxDto> selectDownloadReportByIds(Map<String, Object> map);
	
	/**
	 * 以map为返回值查询SJxx
	 * @param map
	 * @return
	 */
    Map<String,Object> getMapBySjid(Map<String, String> map);

	/**
	 * 根据录入人员获取送检表的合作伙伴
	 * @param sjxxDto
	 * @return
	 */
    List<String> getDbsByLrrys(SjxxDto sjxxDto);
	
	/**
	 * 查询角色检测单位限制
	 * @param jsid
	 * @return
	 */
	List<Map<String,String>> getJsjcdwByjsid(String jsid);

	/**
	 * 根据标本编号获取送检信息
	 * @param ybbhs
	 * @return
	 */
    List<Map<String, String>> getListByYbbhs(List<String> ybbhs);

	/**
	 * 根据标本编号查询送检信息
	 * @param ybbh
	 * @return
	 */
    SjxxDto getDtoByYbbh(String ybbh);

	/**
	 * 送检结果分析列表
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getAnalysisList(SjxxDto sjxxDto);

	/**
	 * 根据外部编码查询送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListByWbbm(SjxxDto sjxxDto);

	/**
	 * 根据外部编码查询送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getDtosByWbbm(SjxxDto sjxxDto);

	/**
	 * 获取报告模板
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getBgmb(SjxxDto sjxxDto);

	/**
	 * 根据旧外部编码查询送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListByLastWbbm(SjxxDto sjxxDto);

	/**
	 * 根据业务ID和业务类型查询下载文件
	 * @param sjxxDtos
	 * @return
	 */
    List<SjxxDto> selectDownloadReportBySjxxDtos(List<SjxxDto> sjxxDtos);
	
	/**
	 * 查询送检信息
	 * @param ybbh
	 * @return
	 */
	SjxxDto getSJxxForSend(String ybbh);
	
	/**
	 * 根据送检ID获取报告模板路径
	 * @param sjid
	 * @return
	 */
    Map<String,String> getWjljBySjid(String sjid);
	
	/**
	 * 获取测试数信息根据检测单位区分
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjxxcssByJcdw(SjxxDto sjxxDto);
	/**
	 * 获取测试数信息根据检测单位区分(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjxxcssByJcdwAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 获取测试数信息根据检测单位区分(日报)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjxxcssByDayAndJcdw(SjxxDto sjxxDto);

	/**
	 * 获取测试数信息根据检测单位区分(日报)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjxxcssByDayAndJcdwAndJsrq(SjxxDto sjxxDto);

	/**
	 * 查询接收后但未实验的标本信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getNotSyList(SjxxDto sjxxDto);

	/**
	 * 查询接收后但未实验的标本信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getNotSyListByJsrq(SjxxDto sjxxDto);

	/**
	 * 首页统计:统计当天实验的数据量(根据检测单位区分)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjlStatisticsByJcdw(SjxxDto sjxxDto);

	/**
	 * 根据条件查询需要下载的报告信息(伙伴限制)
	 * @param map
	 * @return
	 */
    List<SjxxDto> selectDownloadReportSjhb(Map<String, Object> map);

	/**
	 * 查询有word报告的送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> selectWordInfo(SjxxDto sjxxDto);
	
	/**
	 * 查询送检待发送报告
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> querySjxx(SjxxDto sjxxDto);
	
	/**
	 * 获取未发送报告总数
	 * @param sjxxDto
	 * @return
	 */
    int querysjxxnum(SjxxDto sjxxDto);
	
	/**
	 * 获取上传word但未发送报告总数
	 * @param sjxxDto
	 * @return
	 */
    int queryWordNum(SjxxDto sjxxDto);
	
	/**
	 * 获取上传word但未发送报告信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> queryWord(SjxxDto sjxxDto);

	/**
	 * 获取前一天到今天6点前审核中的复测验证数量
	 * @param sjxxDto
	 * @return
	 */
	int queryFjAndYzBeforeNum(SjxxDto sjxxDto);

	/**
	 * 获取前一天到今天6点前审核中的复测验证信息
	 * @param sjxxDto
	 * @return
	 */
	List<SjxxDto> queryFjAndYzBefore(SjxxDto sjxxDto);

	/**
	 * 获取今天6后前审核中的复测验证数量
	 * @param sjxxDto
	 * @return
	 */
	int queryFjAndYzAfterNum(SjxxDto sjxxDto);

	/**
	 * 获取今天6后前审核中的复测验证信息
	 * @param sjxxDto
	 * @return
	 */
	List<SjxxDto> queryFjAndYzAfter(SjxxDto sjxxDto);

	/**
	 * 获取接收日期不为空，还未上传的所有记录
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getAccptNotUpload(SjxxDto sjxxDto);

	/**
	 * 获取接收日期 还未上传的所有记录
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getAccptList(SjxxDto sjxxDto);

	/**
	 * 从送检表中查找一条接收日期不为空，还未上传的单条记录
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getOneFromSJ(SjxxDto sjxxDto);

	/**
	 * 从复检表中查找一条接收日期不为空，还未上传的单条记录
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getOneFromFJ(SjxxDto sjxxDto);
	
	/**
	 * 从送检表中找出检查项目的类别数量
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> jcxmFromSJ(SjxxDto sjxxDto);

	/**
	 * 从复检表中找出检查项目的类别数量
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> jcxmFromFJ(SjxxDto sjxxDto);

	/**
	 * 根据ids(实际传入是fjid)查询送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> selectSjxxByFjids(SjxxDto sjxxDto);

	/**
	 * 修改检测标记（复检表）
	 * @param sjxxDto
	 * @return
	 */
    boolean updateJcbjByFjid(SjxxDto sjxxDto);
	
	/**
	 * 查询前期检测值
	 * @param sjid
	 * @return
	 */
    List<Map<String,Object>> getQqjcForReport(String sjid);

	/**
	 * 同步送检支付信息
	 * @param sjxxDto
	 */
    int modInspPayinfo(SjxxDto sjxxDto);

	/**
	 * 根据送检ID查询发送报告信息
	 * @param params
	 * @return
	 */
    List<SjxxDto> selectReportInfo(Map<String,Object> params);
	
	/**
	 * 根据岗位名称查询用户信息
	 * @param userDto
	 * @return
	 */
    List<UserDto> queryYhByGwmc(UserDto userDto);
	/**
	 * 根据角色名称查询用户
	 * @param userDto
	 * @return
	 */
    List<UserDto> queryYhByJsmc(UserDto userDto);

	/**
	 * 清空检测子项目
	 * @param sjxxDto
	 * @return
	 */
    int emptySubDetect(SjxxDto sjxxDto);

	/**
	 * 优化送检列表查询(内部拆分)
	 * @param params
	 * @return
	 */
    List<SjxxDto> getDtoListOptimize(Map<String, Object> params);

	/**
	 * 优化送检列表查询(数量)
	 * @param params
	 * @return
	 */
    int getCountOptimize(Map<String, Object> params);
	
	
	/**
	 * 查询所有伙伴
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getAllSjhb(SjxxDto sjxxDto);

	/**
	 * 查询送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getBbsList(SjxxDto sjxxDto);
	/**
	 * 查询送检信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getBbsListByJsrq(SjxxDto sjxxDto);


	/**
	 * 得到快递号不为null的信息
	 * @return
	 */
    Set<String> getKdhIsNotNullList(SjxxDto sjxxDto);

	/**
	 * 快递已签收的，更新送检表中顺丰标记字段为1
	 * @param kdhEndSet
	 * @return
	 */
    boolean updateSfbj(List<String> kdhEndSet);

	/**
	 * 快递信息无法查找到的，更新送检表顺丰标记为2
	 * @param kdhEndSet
	 * @return
	 */
    boolean updateUnfindSfbj(List<String> kdhEndSet);
	
    /**
     * 得到送检表中的送检ID和快递号(快递号不去除重复)
     * @param sjxxDto
     * @return
     */
    List<SjxxDto> getSjidAndKdhList(SjxxDto sjxxDto);
	/**
	 * 时效性统计
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getSxx(SjxxDto sjxxDto);
	/**
	 * 复测率统计
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getFcl(SjxxDto sjxxDto);
	/**
	 * 送检单位转义
	 * @param sjxxDto
	 * @return
	 */
    String escapeSjdw(SjxxDto sjxxDto);
	/**
	 * 科室转义
	 * @param sjxxDto
	 * @return
	 */
    String escapeKs(SjxxDto sjxxDto);
	/**
	 * 其它
	 * @param
	 * @return
	 */
    String getQt();
	/**
	 * 其它
	 * @param
	 * @return
	 */
    String getYyxxQt();

//	/**
//	 * 通过样本编号获取相关的信息
//	 * @return
//	 */
//	public List<SjxxDto> getInfoByYbbh(SjxxDto sjxxDto);
	/**
	 * sjxx导入
	 * @param sjxxDto
	 * @return
	 */
    boolean insertSjxx(SjxxDto sjxxDto);


	/**
	 * 查询当日标本编号和复检原因
	 * @return
	 */
    List<SjxxDto> getYbbhAndYyToday();

	/**
	 * 根据样本编号查找送检信息
	 * @param ybbh
	 * @return
	 */
    List<SjxxDto> getDtosByYbbh(String ybbh);

	/**
	 *  根据样本编号模糊查询送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getDtoVague(SjxxDto sjxxDto);


	/**
	 * 查找接收日期为空的可废弃数据
	 * @param sjxxDto
	 * @return
	 */
    List<String> getJsrqIsNull(SjxxDto sjxxDto);

	/**
	 * 获取样本编号对应检测项目以及合作伙伴的参数扩展3
	 * @param ybbh
	 * @return
	 */
	SjxxDto getSjxxGlCskz3(String ybbh);

	/**
	 * 通过检测单位限制查找数据
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getInfo(SjxxDto sjxxDto);

	/**
	 * 修改特殊申请
	 * @param sjxxDto
	 * @return
	 */
    boolean updateTssq(SjxxDto sjxxDto);
	/*
	 * 获取实付总金额和退款总金额
	 * */
	SjxxDto getSfzjeAndTkzje(SjxxDto sjxxDto);

	/**
	 * 根据样本编号查询送检基本信息
	 * @param sjxxDto
	 * @return
	 */
	SjxxDto getSjxxInfoByYbbh(SjxxDto sjxxDto);
	/**
	 * 获取十天内的芯片信息
	 * @return
	 */
	List<SjxxDto> getPagedDtoListDays(SjxxDto sjxxDto);
	/**
	 * 样本进度列表
	 * @return
	 */
	List<SjxxDto> getPagedSampleProgressList(SjxxDto sjxxDto);
	/**
	 * 获取实验员列表头部数据
	 * @return
	 */
    Map<String, Object> querySjSyxxnums(SjxxDto sjxxDto);

	/**
	 * 查询所有送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getPagedExperimentDtoList(SjxxDto sjxxDto);

	/**
	 * 已接受未实验数据
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getConfirmDtoList(SjxxDto sjxxDto);
	
	/**
	 * 更新取样信息
	 * @param sjxxDto
	 * @return
	 */
    boolean updateSampleInfo(SjxxDto sjxxDto);
	/**
	 * 测试数统计
	 * @param sjxxDto
	 * @return
	 */
    List<String> testNumberStatistics(SjxxDto sjxxDto);
	/**
	 * 测试数统计信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> testNumberList(SjxxDto sjxxDto);

	/**
	 * 样本类型代码转义
	 * @param sjxxDto
	 * @return
	 */
    String escapeYblxdm(SjxxDto sjxxDto);

	/**
	 * 根据送检ID和检测类型获取送检数据
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getSjxxDtosByYbbh(SjxxDto sjxxDto);


	/**
	 * 修改检测单位
	 * @param sjxxDto
	 * @return
	 */
    boolean updateJcdw(SjxxDto sjxxDto);
	/**
	 * 处理修改
	 * @param sjxxDto
	 * @return
	 */
    boolean updateDealInfo(SjxxDto sjxxDto);
	
	/**
	 * 查询极简送检信息，不关联其他表
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getSimpleDto(SjxxDto sjxxDto);

    void updateSfjsInfo(List<SjxxDto> sjxxDtoList);

	Map<String,String> getDtoMap(SjxxDto sjxxDto);
	/**
	 * 钉钉小程序端根据样本编号获取下机时间
	 * @param sjxxDto
	 * @return
	 */
	List<SjxxDto> getQdListByYbbh(SjxxDto sjxxDto);

	/**
	 *更新应付金额
	 * @param sjxxDtos
	 * @return
	 */
	boolean updateFkje(List<SjxxDto> sjxxDtos);

	/**
	 * 查找类似标本
	 * @param sjxxDto
	 * @return
	 */
	List<SjxxDto> getSimilarList(SjxxDto sjxxDto);
	/**
	 * 查找类似标本项目
	 * @param sjxxDto
	 * @return
	 */
	List<SjxxDto> getSimilarSjxxJcxmList(SjxxDto sjxxDto);

	List<SjwzxxDto> selectWzxxBySjidAndJclx(SjxxDto sjxxDto);


	/**
	 * 获取定时删除的样本
	 * @param sjxxDto
	 * @return
	 */
	List<String> getDeadlineSamples(SjxxDto sjxxDto);

	/**
	 * 根据自动化编码获取标本实验信息
	 * @param map
	 * @return
	 */
	List<Map<String,String>> getSyxxByZdhbm(Map<String,String> map);

    List<Map<String, Object>> getAbnormalReportList(Map<String, Object> searchMap);

	/**
	 * @Description: 根据sjid查询检测项目
	 * @param sjid
	 * @return com.matridx.igams.wechat.dao.entities.SjxxDto
	 * @Author: 郭祥杰
	 * @Date: 2025/2/25 16:22
	 */
	SjxxDto selectReportBySjid(String sjid);

	/**
	 * 修改送检信息根据sjid
	 * @param sjxxDto
	 * @return
	 */
	int updateByid(SjxxDto sjxxDto);

	
	List<Map<String,String>>getWkcxBySjid(SjxxDto sjxxDto);


	List<Map<String,String>>getWkcxByWkbh(Map<String,String> map);

	List<Map<String,String>>getNcPcByWkbh(Map<String,String> map);

    boolean updateWbbmByList(List<SjxxDto> sjxxDtos);
	
	/**
	 *
	 * 根据文件名获取fjcfb
	 * @param map
	 * @return
	 */
	List<Map<String,String>>getFjByWjm(Map<String,String> map);

	/**
	 * @Description: 根据样本编号查找送检信息
	 * @param ybbh
	 * @return java.util.List<com.matridx.igams.wechat.dao.entities.SjxxDto>
	 * @Author: 郭祥杰
	 * @Date: 2025/8/14 16:15
	 */
	List<SjxxDto> queryByYbbh(String ybbh);
}
