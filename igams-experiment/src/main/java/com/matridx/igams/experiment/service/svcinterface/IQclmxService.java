package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.QclmxDto;
import com.matridx.igams.experiment.dao.entities.QclmxModel;

public interface IQclmxService extends BaseBasicService<QclmxDto, QclmxModel>{
	
	/**
	 * 查询当前最大的序号
	 */
	int getMaxXh(String qclid);
	
	/**
	 * 通过内部编号查询送检id
	 */
	String getSjidByNbbh(String nbbh);
}
