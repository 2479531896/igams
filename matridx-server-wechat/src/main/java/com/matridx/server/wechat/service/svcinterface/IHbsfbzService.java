package com.matridx.server.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.HbsfbzDto;
import com.matridx.server.wechat.dao.entities.HbsfbzModel;

import java.util.List;

public interface IHbsfbzService extends BaseBasicService<HbsfbzDto, HbsfbzModel>{
	
	/**
	 * 获取默认收费标准
	 * @param hbsfbzDto
	 * @return
	 */
    HbsfbzDto getDefaultDto(HbsfbzDto hbsfbzDto);

	/**
	 * 添加收费标准
	 * @param
	 * @return
	 */
    boolean insertsfbz(List<HbsfbzDto> hbsfbzDto);

	/**
	 * 批量修改
	 * @param
	 * @return
	 */
	Boolean updateList(List<HbsfbzDto> list);

	boolean batchModSfbz(List<HbsfbzDto> list);
}
