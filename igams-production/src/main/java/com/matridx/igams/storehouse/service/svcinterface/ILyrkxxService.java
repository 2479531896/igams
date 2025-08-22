package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.LyrkxxDto;
import com.matridx.igams.storehouse.dao.entities.LyrkxxModel;

import java.util.List;

public interface ILyrkxxService extends BaseBasicService<LyrkxxDto, LyrkxxModel>{
	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
    boolean insertList(List<LyrkxxDto> list);

}
