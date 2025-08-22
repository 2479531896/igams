package com.matridx.igams.storehouse.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.DhthclDto;
import com.matridx.igams.storehouse.dao.entities.DhthclModel;

@Mapper
public interface IDhthclDao extends BaseBasicDao<DhthclDto, DhthclModel>{

	/**
	 * 根据类型和货物ID获取退回信息
	 * @param dhthclDto
	 * @return
	 */
	DhthclDto getDtoByHwidAndLx(DhthclDto dhthclDto);
	/**
	 * 获取到货退回的审核信息
	 * @param dhthList
	 * @return
	 */
	List<DhthclDto> getAuditListDhthcl(List<DhthclDto> dhthList);
	
	/**
	 * 从数据库分页获取导出数据
	 * @return
	 */
	List<DhthclDto> getListForSelectExp(DhthclDto dhthclDto);
	/**
	 * 从数据库分页获取导出数据
	 * @return
	 */
	List<DhthclDto> getListForSearchExp(DhthclDto dhthclDto);
	/**
	 * 根据搜索条件获取导出条数
	 * @return
	 */
	int getCountForSearchExp(DhthclDto dhthclDto);
}
