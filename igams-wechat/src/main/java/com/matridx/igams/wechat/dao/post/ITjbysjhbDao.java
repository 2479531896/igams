
package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ITjbysjhbDao extends BaseBasicDao<SjxxDto, SjxxModel>{
	/**
	 * 统计今日实验数
	 * @param sjxxDto
	 * @return
	 */
    int getJcbjBySyrqAndDb(SjxxDto sjxxDto);
	
	/**
	 * 查询当天录入标本数
	 * @param sjxxDto
	 * @return
	 */
	Map<String,Object> getLrYbslBydayAndDb(SjxxDto sjxxDto);
	
	/**
	 * 查询前一天收到的标本并且今天已经产生报告的标本数量
	 * @param sjxxDto
	 * @return
	 */
	Map<String,Object> getybwcd(SjxxDto sjxxDto);
	
	/**
	 * 根据标本类型统计标本信息
	 * @return
	 */
	List<Map<String, String>> getStatisByYblxAndDb(SjxxDto sjxxDto);

	/**
	 * 按照日期统计标本个数，可按照日，月，年，显示指定前几个,需传递起始时间和时间列表
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getStatisYblxByJsrqAndDb(SjxxDto sjxxDto);
	
	/**
	 * 按照日期统计送检报告个数，可按照日，月，年，显示指定前几个,需传递起始时间和时间列表
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getStatisSjbgByBgrqAndDb(SjxxDto sjxxDto);
	
	/**
	 * 按照合作伙伴进行统计
	 * @return
	 */
	List<Map<String, String>> getStatisBysjhbAndDb(SjxxDto sjxxDto);

	/**
	 * 按照周对合作伙伴进行统计
	 * @return
	 */
	List<Map<String, String>> getStatisWeekBysjhbAndDb(SjxxDto sjxxDto);

	/**
	 * 高关注度
	 * @return
	 */
	List<Map<String, String>> getWzxxByGgzdAndDb(SjxxDto sjxxDto);

	/**
	 * 统计结果类型为高关，疑似以及无的标本数量
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getYbnumByJglxAndDb(SjxxDto sjxxDto);
	
	/**
	 * 统计每天标本的阳性率
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getStatisYxlBybgrqAndDb(SjxxDto sjxxDto);

	/**
	 * 查询当天标本数量
	 * @param sjxxDto
	 * @return
	 */
	Map<String,Object> getYbslBydayAndDb(SjxxDto sjxxDto);

	/**
	 * 获取最近一周标本送检路线
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String,Object>> getSjxlxxAndDb(SjxxDto sjxxDto);

	/**
	 * 根据收费情况进行统计收样(按照不收费的标本变化，收费标本的变化)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjxxBySyAndDb(SjxxDto sjxxDto);
	
	/**
	 * 根据收费情况以周为单位进行统计收样(按照不收费的标本变化，收费标本的变化)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjxxWeekBySyAndDb(SjxxDto sjxxDto);
	
}
