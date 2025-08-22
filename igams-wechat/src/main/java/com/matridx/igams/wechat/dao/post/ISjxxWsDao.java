package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISjxxWsDao extends BaseBasicDao<SjxxDto, SjxxModel>{

	/**
	 * 钉钉小程序获取送检列表
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getMiniSjxxList(SjxxDto sjxxDto);


	/**
	 * ddid获取到检测单位
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getJcdwByDdid(SjxxDto sjxxDto);

	/**
	 * 钉钉小程序获取未反馈列表
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getMiniFeedbackList(SjxxDto sjxxDto);

	/**
	 * 微信小程序获取送检列表
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getWeChatSjxxList(SjxxDto sjxxDto);

	/**
	 * 微信小程序获取未反馈列表
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getWeChatFeedbackList(SjxxDto sjxxDto);
	
	/**
	 * 更新报告日期
	 * @param sjxxDto
	 * @return
	 */
    int updateReport(SjxxDto sjxxDto);

	/**
	 * 根据内部编码和检测项目获取最近一条文库信息
	 * @return
	 */
    Map<String,Object> getRecentlyLibraryInfo(Map<String, Object> map);

	/**
	 * 获取文库信息list
	 * @return
	 */
    List<Map<String,Object>> getLibraryList(Map<String, Object> map);

	/**
	 * 获取报告模板
	 * @param map
	 * @return
	 */
    Map<String,Object> getReportMould(Map<String, String> map);

	/**
	 * 补充数据用需删除
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getDtoByLrsj(SjxxDto sjxxDto);

	/**
	 * 补充数据用需删除
	 * @param sjxxDto
	 * @return
	 */
    List<FjsqDto> getFjDtoByLrsj(SjxxDto sjxxDto);

	List<Map<String,Object>> getTqmxBySjid(String sjid);

	List<Map<String,String>> getYbbhListByWbInfo(Map<String,Object> map);
	List<Map<String,String>> getYbbhListByWbInfoPlus(Map<String,Object> map);
	List<Map<String,String>> getJcxmFjInfoByYwids(Map<String,Object> map);
	List<Map<String,String>> getJcxmFjInfoByYwidsPlus(Map<String,Object> map);
	List<Map<String,String>> getSjxxInfo(List<Map<String, String>> list);
	/**
	 * 获取文库信息
	 * @param map
	 * @return
	 */
	List<Map<String,String>> selectWksjInfo(Map<String,String>map);

	/**
	 * 获取默认报告模板
	 * @param map
	 * @return
	 */
	Map<String,Object> getDefaultPartner(Map<String, String> map);
}
