package com.matridx.las.netty.service.svcinterface;

import com.matridx.igams.common.dao.entities.WkmxPcrModel;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.netty.dao.entities.DlysymxDto;
import com.matridx.las.netty.dao.entities.DlysymxModel;

public interface IDlysymxService extends BaseBasicService<DlysymxDto, DlysymxModel>{
	/**
	 * 保存定量仪明细结果信息
	 * @param pcr_result
	 * @return
	 */
	public WkmxPcrModel saveDlysymx(String pcr_result) ;

}
