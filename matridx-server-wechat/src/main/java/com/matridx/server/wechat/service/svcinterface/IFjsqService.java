package com.matridx.server.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.FjsqDto;
import com.matridx.server.wechat.dao.entities.FjsqModel;

import java.util.List;

public interface IFjsqService extends BaseBasicService<FjsqDto, FjsqModel> {
	
	/**
	 * 送检列表查看复检申请信息
	 * @param fjsqDto
	 * @return
	 */
    List<FjsqDto> getListBySjid(FjsqDto fjsqDto);

}
