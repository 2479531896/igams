package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.LscxszDto;
import com.matridx.igams.common.dao.entities.LscxszModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ILscxszService extends BaseBasicService<LscxszDto, LscxszModel>{

	/**
	 * 根据查询id查询临时查询信息
	 */
	 LscxszDto selectLscxByCxid(LscxszDto lscxszDto);
	
	/**
	 * 获取临时查询结果
	 */
	 List<HashMap<String, Object>> getQueryResult(LscxszDto lscxszDto);

	/**
	 * 执行sql
	 */
	public boolean executeResult(LscxszDto lscxszDto);

	/**
	 * 获取销售查询字段
	 */
	 List<Map<String, String>> getFieldList(String cxdm);

	/**
	 * 查询sql处理
	 */
	 String dealQuerySql(LscxszDto t_lscxszDto, String yhid);
	
	/**
	 * 统计sql处理
	 */
	 String dealStatisticsQuerySql(LscxszDto t_lscxszDto, String yhid);
	
	/**
	 * 获取限制和All的临时查询信息
	 */
	 List<LscxszDto> getPagedDtoListByLimt(LscxszDto lscxszDto);
	
	/**
	 * 获取精简限制的临时查询信息，不包含sql代码
	 */
	 List<LscxszDto> getPagedSimpDtoListByLimt(LscxszDto lscxszDto);
	
	/**
	 * 获取精简限制的临时查询信息，不包含sql代码，主要是针对统计消息发送，里面不需要对用户权限进行限制
	 */
	 List<LscxszDto> getSimpDtoListByCode(LscxszDto lscxszDto);
	
	/**
	 * 获取指定统计区分的临时查询结果
	 */
	 List<LscxszDto> getDtoListByCode(LscxszDto lscxszDto);
	
	/**
	 * 获取查询区分不是LIMIT和STATISTICS的查询
	 */
	 boolean queryByCxid(LscxszDto lscxszDto);

	/**
	 * @Description: 根据查询代码获取临时查询表数据
	 * @param lscxszDto
	 * @return java.util.List<com.matridx.igams.common.dao.entities.LscxszDto>
	 * @Author: 郭祥杰
	 * @Date: 2025/2/27 15:48
	 */
	List<LscxszDto> selectLscxByQfdm(LscxszDto lscxszDto);

	/**
	 * 获取临时查询结果
	 */
	List<LinkedHashMap<String, Object>> getResult(LscxszDto lscxszDto);

	String dealDownloadHuashanData(LscxszDto lscxszDto);

	/**
	 * 定时任务执行相应代码
	 */
	void executeByParam(Map<String, String> params);
}
