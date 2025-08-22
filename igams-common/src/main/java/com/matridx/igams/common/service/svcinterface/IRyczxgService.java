package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.RyczxgDto;
import com.matridx.igams.common.dao.entities.RyczxgModel;

public interface IRyczxgService extends BaseBasicService<RyczxgDto, RyczxgModel>{

	/**
	 * 新增或修改人员操作习惯
	 */
	boolean insertOrUpdate(RyczxgDto ryczxgDto);

}
