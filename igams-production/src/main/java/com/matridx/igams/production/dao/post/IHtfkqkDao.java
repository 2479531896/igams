package com.matridx.igams.production.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.HtfkqkDto;
import com.matridx.igams.production.dao.entities.HtfkqkModel;
import java.util.List;
@Mapper
public interface IHtfkqkDao extends BaseBasicDao<HtfkqkDto, HtfkqkModel>{


	List<HtfkqkDto> getPagedAuditHtfkqk(HtfkqkDto htfkqkDto);

	List<HtfkqkDto> getAuditListHtfkqk(List<HtfkqkDto> list);

	/**
	 *根据ddslid获取合同付款信息
	 */
	HtfkqkDto getDtoByDdslid(HtfkqkDto htfkqkDto);

	/**
	 * 将钉钉实例ID至为空
	 */
	void updateDdslidToNull(HtfkqkDto htfkqkDto);

	/**
	 * 获取流水号
	 */
	String getDjhSerial(String prefix);
	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForSearchExp(HtfkqkDto htfkqkDto);

	/**
	 * 从数据库分页获取导出数据
	 */
	List<HtfkqkDto> getListForSearchExp(HtfkqkDto htfkqkDto);

	/**
	 * 从数据库分页获取导出数据
	 */
	List<HtfkqkDto> getListForSelectExp(HtfkqkDto htfkqkDto);
}
