package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.WechatReferencesModel;

import java.util.List;
import java.util.Map;


public interface ISjwzxxService extends BaseBasicService<SjwzxxDto, SjwzxxModel>{

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
	boolean deleteBysjwzxxDto(SjwzxxDto sjwzxxDto);
	
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
	 * 查询耐药信息
	 * @param sjxxDto
	 * @return
	 */
	List<SjwzxxDto> getNyxByForWzxx(SjxxDto sjxxDto);
	
	/**
	 * 查询检测结果(新模板)
	 * @param sjid
	 * @return
	 */
	Map<String,Object> getPathogensMap(String sjid,String jclx,List<WechatReferencesModel> papers);
	
	/**
	 * 查询检测结果
	 * @param sjid
	 * @return
	 */
    Map<String,Object> getPathogensMapNew(String sjid, String jclx, List<WechatReferencesModel> papers, Map<String, Object> map);
	
	/**
	 * 查询检测结果说明
	 * @param sjxxDto
	 * @return
	 */
	Map<String,Object> getPathogen_commentToString(SjxxDto sjxxDto);
	
	/**
	 * 查询检测结果(旧模板)
	 * @param sjid
	 * @return
	 */
	Map<String,String> getJcjgForOldTemplate(String sjid,String jclx);
	
	/**
	 * 查询检测结果说明(旧模板)
	 * @param sjxxDto
	 * @return
	 */
	Map<String,String> getPathogenForOldTemplate(SjxxDto sjxxDto);

	/**
	 * 查询检测结果说明
	 * @param sjwzxxDto
	 * @return
	 */
	Map<String, String> getComment(SjwzxxDto sjwzxxDto);
	/**
	 * 查询检测结果说明
	 */
	List<SjwzxxDto> getComment(Map<String, String> map);
	
	/**
	 * 查询参考文献（默认）区分高关低关
	 * @param sjid
	 * @param jclx
	 * @return
	 */
	String getReferences_default(String sjid,String jclx,List<WechatReferencesModel> papers);
	
	/**
	 * 查询参考文献（默认）区分高关低关
	 * @param sjid
	 * @param jclx
	 * @return
	 */
    String getReferences_defaultNew(String sjid, String jclx, List<WechatReferencesModel> papers, Map<String, Object> resultMap);
	
	/**
	 * 2023-11-14 注释原因：未使用
	 * 通过sjid和检测类型获取送检物种信息
	 * @param sjwzxxDto
	 * @return
	 */
//    List<SjwzxxDto> selectWzxx(SjwzxxDto sjwzxxDto);
	

	/**
	 * 2023-11-14 注释原因：未使用
	  * 查询检测项目代码和标本类型代码
	 * @param sjid
	 * @return
	 */
//    Map<String,String> getCsdmBySjid(String sjid);

	/**
	 * 根据外部编码获取检测结果信息
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> getJcjgInfoByCodes(List<String> ids);
	
	/**
	 * 查询检测结果(旧模板)
	 * @param sjid
	 * @return
	 */
	Map<String,String> getJcjgForOldTemplateNew(String sjid,String jclx,Map<String,Object> resultmap);
	
	Map<String,Object> getPathogen_commentToStringNew(SjxxDto sjxxDto, Map<String, Object> reMap);
	
	Map<String, Object> getPathogenForOldTemplateNew(SjxxDto sjxxDto, Map<String, Object> resultMap);

	/**
	 * 根据sjids和jclx删除
	 * @param sjwzxxDto
	 * @return
	 */
	boolean deleteBySjidsAndJclx(SjwzxxDto sjwzxxDto);

	/**
	 * 根据sjid和ywlx获取检测结果
	 * @param sjxxDto
	 * @return
	 */
	Map<String, Object> getJcjgInfoByXinghe(SjxxDto sjxxDto);
	
	/*
	 * 批量更新送检物种信息
	 */
    boolean updateBySjwzxxDtos(List<SjwzxxDto> sjwzxxDtos);

    List<Map<String, Object>> searchResult(SjxxDto dto);

	List<SjwzxxDto> getJcjgByJglx(SjwzxxDto sjwzxxDto);
}
