package com.matridx.las.netty.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.netty.dao.entities.WksyDto;
import com.matridx.las.netty.dao.entities.WksyModel;

public interface IWksyService extends BaseBasicService<WksyDto, WksyModel>{
	/**
	 * 保存定量仪结果信息
	 * @param dlysyDto
	 * @return
	 */
	public boolean saveDlysy(WksyDto dlysyDto) ;
	/**
	 * 修改
	 */
	public boolean updateDlysyDto(WksyDto dlysyDto);
	
	public WksyDto getWksyDtoBywkid(WksyDto wksyDto) ;
}
