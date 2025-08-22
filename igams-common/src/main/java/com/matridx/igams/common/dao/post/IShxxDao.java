package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.ShxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IShxxDao extends BaseBasicDao<ShxxDto, ShxxModel>{
	/**
	 * 获得全部审核信息，按审核时间倒排序
	 * dto
	 * 
	 */
	 List<ShxxDto> getShxxOrderByShsj(ShxxDto dto);
	
	/**
	 * 获取最新的一条审核信息
	 * dto
	 * 
	 */
	 ShxxDto getLastShxx(ShxxDto dto);

	/**
	 * 根据过程ID获取审核信息
	 * shxxDto
	 * 
	 */
	 List<ShxxDto> getShxxOrderByGcid(ShxxDto shxxDto);

	/**
	 * 查询业务审核过程
	 * shxxParam
	 * 
	 */
	 List<ShxxDto> getShxxOrderByPass(ShxxDto shxxParam);

	/**
	 * 审核信息修改
	 * shxxDto
	 * 
	 */
	 int auditmodSaveTime(ShxxDto shxxDto);

	 List<ShxxDto> getSecShxxOrderByShsj(ShxxDto shxxDto);

	/**
	 * 获取最新的审核通过的信息
	 * shxxDto
	 * 
	 */
	 List<ShxxDto> getPassShxxBestNew(ShxxDto shxxDto);

	/**
	 * 新冠获取提交信息
	 * shxxDto
	 * 
	 */
	 ShxxDto getPassShxxBestSqr(ShxxDto shxxDto);
	/**
	 * 获取通过信息
	 * shxxDto
	 * 
	 */
	List<ShxxDto> getShxxByYwidsTG(ShxxDto shxxDto);
	/**
	 * 获取提交信息
	 * shxxDto
	 * 
	 */
	List<ShxxDto> getShxxByYwidsTj(ShxxDto shxxDto);
	/**
	 * 根据lcxh分组获取审核信息
	 * shxxDto
	 * 
	 */
	List<ShxxDto> getShxxGroupByLcxh(ShxxDto shxxDto);

	ShxxDto getShxxByYwid(ShxxDto shxxDto);
	List<ShxxDto> getShxxLsit(ShxxDto shxxDto);
	ShxxDto queryShsj(ShxxDto shxxDto);
}
