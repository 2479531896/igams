package com.matridx.igams.storehouse.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.RkglDto;
import com.matridx.igams.storehouse.dao.entities.RkglModel;

@Mapper
public interface IRkglDao extends BaseBasicDao<RkglDto, RkglModel>{

	/**
	 * 查询入库流水号
	 * @param prefix
	 * @return
	 */
	String getRkdhSerial(String prefix);
	
	/**
	 * 通过入库单号查询入库信息
	 * @return
	 */
	List<RkglDto> queryByRkdh(RkglDto rkglDto);
	
	/**
	 * 获取审核列表 
	 * @return
	 */
	List<RkglDto> getPagedAuditRkgl(RkglDto rkglDto);
	
	/**
	 * 审核列表
	 * @return
	 */
	List<RkglDto> getAuditListRkgl(List<RkglDto> list);
	
	/**
	 * 根据搜索条件获取导出条数
	 * @return
	 */
	int getCountForSearchExp(RkglDto rkglDto);
	
	/**
	 * 从数据库分页获取导出数据
	 * @return
	 */
	List<RkglDto> getListForSearchExp(RkglDto rkglDto);
	
	/**
	 * 从数据库分页获取导出数据
	 * @return
	 */
	List<RkglDto> getListForSelectExp(RkglDto rkglDto);
	
	/**
	 * 根据入库ids获取入库信息（共通页面）
	 * @param rkglDto
	 * @return
	 */
	List<RkglDto> getCommonDtoListByRkids(RkglDto rkglDto);
	
	/**
	 * 更新入库关联ID
	 * @param rkglDto
	 * @return
	 */
	boolean updateGldb(RkglDto rkglDto);

	/**
	 * 到货通知需要通知的人员和信息
	 * @return
	 */
	List<RkglDto> getRktzList(RkglDto rkglDto);
	/**
	 * 获取入库列表钉钉
	 * @param rkglDto
	 * @return
	 */
	List<RkglDto> getPagedDtoListDingTalk(RkglDto rkglDto);
}
