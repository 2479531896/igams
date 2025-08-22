package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISjwzxxDao extends BaseBasicDao<SjwzxxDto, SjwzxxModel>{

	/**
	 * 批量新增送检物种信息
	 * @param sjwzxxDtos
	 * @return
	 */
	boolean insertBysjwzxxDtos(List<SjwzxxDto> sjwzxxDtos);

	/**
	 * 批量删除送检物种信息
	 * @param sjwzxxDto
	 * @return
	 */
	int deleteBysjwzxxDto(SjwzxxDto sjwzxxDto);
	
	/**
	 * 报告模板高关低关数据 
	 * @param sjwzxxDto
	 * @return
	 */
	List<SjwzxxDto> selectAttention(SjwzxxDto sjwzxxDto);
	
	/**
	 * 2023-11-14 注释原因：未使用
	 * 通过sjid和检测类型获取送检物种信息
	 * @param sjwzxxDto
	 * @return
	 */
//	List<SjwzxxDto> selectWzxxForWord(SjwzxxDto sjwzxxDto);
	
	/**
	 * 根据查询送检耐药性
	 * @param sjxxDto
	 * @return
	 */
	List<SjwzxxDto> getNyxByForWzxx(SjxxDto sjxxDto);

	/**
	 * 查询检测结果
	 * @param map
	 * @return
	 */
	List<SjwzxxDto> getJcjgForWord(Map<String,String> map);
	
	/**
	 * 查询检测结果说明
	 * @param map
	 * @return
	 */
	List<SjwzxxDto> getPathogen_comment(Map<String,String> map);
	
	/**
	 * 2023-11-14 注释原因：未使用
	 * 查询检测项目代码和标本类型代码
	 * @param sjid
	 * @return
	 */
//	Map<String,String> getCsdmBySjid(String sjid);
	/**
	 * 根据外部编码获取检测结果信息
	 * @param ids
	 * @return
	 */
	List<Map<String,String>> getJcjgInfoByCodes(List<String> ids);
	/**
	 * 根据外部编码获取检测结果信息
	 * @param ids
	 * @return
	 */
	List<Map<String,String>> getJcjgInfoBySjidAndYwlx(SjxxDto sjxxDto);
	/**
	 * 根据外部编码获取检测结果类型
	 * @param id
	 * @return
	 */
	List<Map<String,String>> getJcjgLxByCode(String id);

	/**
	 * 按模板顺序查询检测结果
	 * @param map
	 * @return
	 */
	List<SjwzxxDto> getJcjgForWordNew(Map<String,Object> map);

	/**
	 * 查询检测结果说明
	 * @param map
	 * @return
	 */
	List<SjwzxxDto> getComment(Map<String, String> map);
	/**
	 * 按模板顺序查询Background检测结果
	 * @param map
	 * @return
	 */
	List<SjwzxxDto> getBackgroundJcjgForWordNew(Map<String,Object> map);
	/**
	 * 按模板顺序查询Commensal检测结果
	 * @param map
	 * @return
	 */
	List<SjwzxxDto> getCommensalJcjgForWordNew(Map<String,Object> map);
	
	/**
	 * 按模板顺序查询检测结果
	 * @param sjwzxxDtos
	 * @return
	 */
	List<SjwzxxDto> getJcjgByJglx(SjwzxxDto  sjwzxxDtos);

	/**
	 * 根据sjids和jclx删除
	 * @param sjwzxxDto
	 * @return
	 */
	boolean deleteBySjidsAndJclx(SjwzxxDto sjwzxxDto);
	/*
	 * 批量更新送检物种信息
	 */
	boolean updateBySjwzxxDtos(List<SjwzxxDto> sjwzxxDtos);

    List<Map<String, Object>> searchResult(SjxxDto dto);
}
