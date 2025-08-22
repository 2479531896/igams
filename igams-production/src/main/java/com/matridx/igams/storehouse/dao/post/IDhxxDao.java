package com.matridx.igams.storehouse.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.DhxxDto;
import com.matridx.igams.storehouse.dao.entities.DhxxModel;

@Mapper
public interface IDhxxDao extends BaseBasicDao<DhxxDto, DhxxModel>{
	/**
	 * 获取审核列表 
	 * @return
	 */
	List<DhxxDto> getPagedAuditDhxx(DhxxDto dhxxDto);
	
	/**
	 * 审核列表
	 * @return
	 */
	List<DhxxDto> getAuditListDhxx(List<DhxxDto> list);
	
	/**
	 * 获取到货列表
	 * @param dhxxDto
	 * @return
	 */
	List<DhxxDto> getListByDhdh(DhxxDto dhxxDto);
	
	/**
	 * 获取流水号
	 * @param dhdh
	 * @return
	 */
	String getDhdhSerial(String dhdh);
	
	/**
	 * 根据搜索条件获取导出条数
	 * @return
	 */
	int getCountForSearchExp(DhxxDto dhxxDto);
	
	/**
	 * 从数据库分页获取导出数据
	 * @return
	 */
	List<DhxxDto> getListForSearchExp(DhxxDto dhxxDto);
	
	/**
	 * 从数据库分页获取导出数据
	 * @return
	 */
	List<DhxxDto> getListForSelectExp(DhxxDto dhxxDto);
	
	/**
	 * 根据到货信息查货物信息
	 * @return
	 */
	List<DhxxDto> getListBydhid(DhxxDto dhxxDto);
	/**
	 * 根据到货ids获取到货信息（共通页面）
	 * @param dhxxDto
	 * @return
	 */
	List<DhxxDto> getCommonDtoListByDhids(DhxxDto dhxxDto);

	/**
	 *   根据到货id更新确认人员/时间/标记信息
	 * @param dhxxDto
	 * @return
	 */
	boolean updateConfirmDhxx(DhxxDto dhxxDto);

	/**
	 * 查找到货列表的借用总量和归还总量
	 * @return
	 */
	List<DhxxDto> getJyzlAndGhzlList();

	/**
	 * 更改到货信息的请验单号
	 * @param dhxx
	 */
	void updateDhxxQydh(DhxxDto dhxx);
	
	/**
	 * 生成请验单号
	 * @param prefix
	 * @return
	 */
	String generteQydh(String prefix);
	/**
	 * 到货列表钉钉
	 */
	List<DhxxDto> getPagedDtoListDingTalk(DhxxDto dhxxDto);
	
		/**
	 * 出入库统计
	 * @param dhxxDto
	 * @return
	 */
		List<DhxxDto> getCountStatistics(DhxxDto dhxxDto);

    boolean insertDhxxDtos(List<DhxxDto> dhxxDtos);
}
