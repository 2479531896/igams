package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxModel;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

public interface ITjbysjhbService extends BaseBasicService<SjxxDto, SjxxModel>{

	/**
	 * 标本统计页面，跟统计接口一样
	 * @return
	 */
    ModelAndView getStatisPage();
	
	/**
	 * 高关注度
	 * @return
	 */
    List<Map<String, String>> getWzxxByGgzdAndDb(SjxxDto sjxxDto);
	
	/**
	 * 按照送检单位统计个数。点击后按照送检医生排名
	 * @return
	 */
    List<Map<String, String>> getStatisBysjhbAndDb(SjxxDto sjxxDto);
	
	/**
	 * 按照周送检单位统计个数。点击后按照送检医生排名
	 * @return
	 */
    List<Map<String, String>> getStatisWeekBysjhbAndDb(SjxxDto sjxxDto);

	/**
	 * 根据日期查询每天标本数量
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByDateAndDb(SjxxDto sjxxDto);
	
	/**
	 * 根据标本类型统计标本信息(按照日期)
	 * @return
	 */
    List<Map<String, String>> getStatisByYblxAndDb(SjxxDto sjxxDto);
	
	/**
	 * 统计结果类型为高关，疑似以及无的标本数量
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getYbnumByJglxAndDb(SjxxDto sjxxDto);

	/**
	 * 查询当天标本数量
	 * @param sjxxDto
	 * @return
	 */
    Map<String,Object> getYbslBydayAndDb(SjxxDto sjxxDto);
	
	/**
	 * 按照日期统计送检报告个数，可按照日，月，年，显示指定前几个,需传递起始时间和时间列表
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisSjbgByBgrqAndDb(SjxxDto sjxxDto);
	
	/**
	 * 统计每天标本的阳性率
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisYxlBybgrqAndDb(SjxxDto sjxxDto);

	/**
	 * 查询送检报告完成度
	 * @param sjxxDto
	 * @return
	 */
    Map<String,Object> getBgWcd(SjxxDto sjxxDto);
	
	/**
	 * 获取最近一周标本送检路线
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,Object>> getSjxlxxAndDb(SjxxDto sjxxDto);
	
	/**
	 * 根据收费情况进行统计收样
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
