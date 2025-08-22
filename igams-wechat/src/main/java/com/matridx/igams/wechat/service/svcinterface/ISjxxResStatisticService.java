package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.wechat.dao.entities.SjxxDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ISjxxResStatisticService
{
	/**
	 * 统计ResFirst™当日情况
	 * @param
	 * @return
	 */
	Map<String, String> getRYbStatisByDay(SjxxDto sjxxDto);
	/**
	 * ResFirst™日报统计
	 * @return
	 */
    List<Map<String, String>> getRYbxxByDay(SjxxDto sjxxDto);
	/**
	 * 送检复检实验数
	 * @param
	 * @return
	 */
	Map<String, Integer> getSjFjNum(SjxxDto sjxxDto);
	/**
	 * @description 获取各个周期平均用时
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getAvgTime(SjxxDto sjxxDto);
	/**
	 * @description 获取各个周期个数
	 * @param sjxxDto
	 * @return
	 */
	Map<String, String>  getLifeCount(SjxxDto sjxxDto);
	/**
	 * @description 获取生命周期
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getLifeCycle(SjxxDto sjxxDto);
	/**
	 * 根据接收日期的开始日期和结束日期，自动计算每一天的日期，形成一个list
	 *
	 * @param sjxxDto
	 * @return
	 */
    List<String> getRqsByStartAndEnd(SjxxDto sjxxDto);
	/**
	 * @description 各周期标本用时
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String, String>> getLifeTimeCount(SjxxDto sjxxDto);
	
	/**
	 * 接收天隆上传的文件，并对结果进行保存
	 * @param file
	 * @param params
	 * @return
	 */
	String uploadResFirstResult(MultipartFile file,Map<String, String> params);
}
