package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.CxyxxDto;
import com.matridx.igams.experiment.dao.entities.CxyxxModel;

import java.util.List;

public interface ICxyxxService extends BaseBasicService<CxyxxDto, CxyxxModel>{

	/**
	 * 更新测序仪信息
	 */
	boolean updateCxyxx(CxyxxDto cxyxxDto) throws BusinessException;

	List<CxyxxDto> getDtoByMcOrCxybh(CxyxxDto cxyxxDto);

}
