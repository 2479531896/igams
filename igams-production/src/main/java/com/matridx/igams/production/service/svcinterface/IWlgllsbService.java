package com.matridx.igams.production.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.WlgllsbDto;
import com.matridx.igams.production.dao.entities.WlgllsbModel;

public interface IWlgllsbService extends BaseBasicService<WlgllsbDto, WlgllsbModel>{

	/**
	 * 物料修改申请审核列表
	 */
	List<WlgllsbDto> getPagedModAuditList(WlgllsbDto wlgllsbDto);

	/**
	 * 通过物料id查询临时表数据
	 */
	List<WlgllsbDto> getDtoListByWlid(WlgllsbDto wlgllsbDto);

	/**
	 * 物料临时修改提交保存
	 */
	WlgllsbDto modSubmitSaveMaterTemp(WlgllsbDto wlgllsbDto);
}
