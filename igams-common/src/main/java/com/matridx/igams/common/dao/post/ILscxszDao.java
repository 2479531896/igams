package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.LscxszDto;
import com.matridx.igams.common.dao.entities.LscxszModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Mapper
public interface ILscxszDao extends BaseBasicDao<LscxszDto, LscxszModel> {

	/**
	 * 根据查询id查询临时查询信息
	 * 
	 * lscxszDto
	 * 
	 */
	 LscxszDto selectLscxByCxid(LscxszDto lscxszDto);

	/**
	 * 获取临时查询结果
	 * 
	 * lscxszDto
	 * 
	 */
	 List<HashMap<String, Object>> getQueryResult(LscxszDto lscxszDto);

	/**
	 * 获取限制的临时查询结果
	 * 
	 * lscxszDto
	 * 
	 */
	 List<LscxszDto> getPagedDtoListByLimt(LscxszDto lscxszDto);

	/**
	 * 获取精简限制的临时查询信息，不包含sql代码
	 * lscxszDto
	 * 
	 */
	 List<LscxszDto> getPagedSimpDtoListByLimt(LscxszDto lscxszDto);

	/**
	 * 获取指定统计区分的临时查询信息，不包含sql代码，主要是针对统计消息发送，里面不需要对用户权限进行限制
	 * lscxszDto
	 * 
	 */
	List<LscxszDto> getSimpDtoListByCode(LscxszDto lscxszDto);
	
	/**
	 * 获取指定统计区分的临时查询结果
	 * 
	 * lscxszDto
	 * 
	 */
	List<LscxszDto> getDtoListByCode(LscxszDto lscxszDto);
	
	/**
	 * 获取查询区分不是LIMIT和STATISTICS的查询
	 * lscxszDto
	 * 
	 */
	List<LscxszDto> queryByCxid(LscxszDto lscxszDto);

	/**
	 * 执行sql
	 */
	int executeResult(LscxszDto lscxszDto);

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
}
