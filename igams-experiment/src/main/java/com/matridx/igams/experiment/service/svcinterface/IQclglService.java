package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.QclglDto;
import com.matridx.igams.experiment.dao.entities.QclglModel;

public interface IQclglService extends BaseBasicService<QclglDto, QclglModel>{

	/**
	 * 删除前处理管理和前处理明细
	 */
	boolean deleteQclgl(QclglDto qclglDto);
}
