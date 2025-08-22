package com.matridx.igams.production.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.WlgllsbDto;
import com.matridx.igams.production.dao.entities.WlgllsbModel;

@Mapper
public interface IWlgllsbDao extends BaseBasicDao<WlgllsbDto, WlgllsbModel>{

	/**
	 * 更新物料修改的审核状态
	 */
	void updateZt(WlgllsbModel wlgllsbModel);

	/**
	 * 获取物料修改申请审核的ID列表
	 */
	List<WlgllsbDto> getPagedModAuditIdList(WlgllsbDto wlgllsbDto);

	/**
	 * 根据物料修改申请审核的ID列表获取审核列表详细信息
	 */
	List<WlgllsbDto> getModAuditListByIds(List<WlgllsbDto> t_sqList);
	
	/**
	 * 更新临时表数据审核后状态
	 */
	void updateSc(WlgllsbDto wlgllsbDto);

	/**
	 * 通过物料id查询临时表数据
	 */
	List<WlgllsbDto> getDtoListByWlid(WlgllsbDto wlgllsbDto);

	/**
	 * 根据物料ids查询临时表信息
	 */
	List<WlgllsbDto> getDtoListByWlids(WlgllsbDto wlgllsbDto);

}
