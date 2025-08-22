package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.HbbgmbglDto;
import com.matridx.igams.wechat.dao.entities.HbbgmbglModel;

import java.util.List;

public interface IHbbgmbglService extends BaseBasicService<HbbgmbglDto, HbbgmbglModel> {
	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
    boolean insertList(List<HbbgmbglDto> list);

}
