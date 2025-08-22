package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import java.util.List;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.SpgwcyModel;

public interface ISpgwcyService extends BaseBasicService<SpgwcyDto, SpgwcyModel>{

	/**
	 * 获取岗位可选成员
	 */
	 List<SpgwcyDto> getPagedOptionalList(SpgwcyDto spgwcyDto);

	/**
	 * 获取岗位已选成员
	 */
	 List<SpgwcyDto> getPagedSelectedList(SpgwcyDto spgwcyDto);

	/**
	 * 添加岗位成员
	 */
	 boolean toSelected(SpgwcyDto spgwcyDto);

	/**
	 * 去除岗位成员
	 */
	 boolean toOptional(SpgwcyDto spgwcyDto);
	/**
	 * 获取审批岗位成员单位限制
	 */
    List<SpgwcyDto> getDtoListWithDW(SpgwcyDto spgwcyDto);
}
