package com.matridx.igams.common.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.LbcxszDto;
import com.matridx.igams.common.dao.entities.LbcxszModel;

@Mapper
public interface ILbcxszDao extends BaseBasicDao<LbcxszDto, LbcxszModel>{

	/**
	 * 将新增数据增加到系统资源表
	 */
	 int insertXtzy(LbcxszDto lbcxszDto);

	/**
	 * 将新增数据增加到资源操作表
	 */
	 int insertZyczb(LbcxszDto lbcxszDto);

	/**
	 * 查询系统资源表
	 */
	 LbcxszDto getXtzyByZylj(String zylj);

	/**
	 * 查询资源操作表
	 */
	 List<LbcxszDto> getZyczbByZyid(String zyid);

	/**
	 * 修改系统资源信息
	 */
	 int updateXtzy(LbcxszDto lbcxszDto);

	/**
	 * 修改资源操作表信息
	 */
	 int updateZyczb(LbcxszDto lbcxszDto);

	/**
	 * 更新列表查询设置的删除状态
	 */
	 int updateLbcxszScbj(LbcxszDto lbcxszDto);

	/**
	 * 删除系统资源表数据
	 * lbcxszDto
	 * 
	 */
	 int deleteXtzy(LbcxszDto lbcxszDto);

	/**
	 * 删除资源操作表数据
	 * lbcxszDto
	 * 
	 */
	 int deleteZyczb(LbcxszDto lbcxszDto);

	/**
	 * 获取操作代码同时根据操作代码名称标识操作代码信息
	 * lbcxszDto
	 * 
	 */
	 List<LbcxszDto> getAllCzdmAndChecked(LbcxszDto lbcxszDto);

	/**
	 * 判断zyid是否重复
	 * lbcxszDto
	 * 
	 */
	 List<LbcxszDto> selectToZyid(LbcxszDto lbcxszDto);

	/**
	 * 判断修改zyid是否重复
	 * lbcxszDto
	 * 
	 */
	 List<LbcxszDto> selectToModZyid(LbcxszDto lbcxszDto);

	/**
	 * 判断ywid是否重复
	 * lbcxszDto
	 * 
	 */
	 List<LbcxszDto> selectToYwid(LbcxszDto lbcxszDto);

	/**
	 * 判断修改ywid是否重复
	 * lbcxszDto
	 * 
	 */
	 List<LbcxszDto> selectToModYwid(LbcxszDto lbcxszDto);

}
