package com.matridx.igams.storehouse.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.DhthclDto;
import com.matridx.igams.storehouse.dao.entities.DhthclModel;

public interface IDhthclService extends BaseBasicService<DhthclDto, DhthclModel>{

	/**
	 * 根据类型和货物ID获取退回信息
	 * @param dhthclDto
	 * @return
	 */
	DhthclDto getDtoByHwidAndLx(DhthclDto dhthclDto);
	/**
	 * 查询到货退回数据
	 * @param dhthclDto
	 * @return
	 */
	List<DhthclDto> getPagedDtoStockList(DhthclDto dhthclDto);
	/**
	 * 导出数量
	 * @param dhthclDto
	 * @param params
	 * @return
	 */
	int getCountForSearchExp(DhthclDto dhthclDto, Map<String, Object> params);
}
