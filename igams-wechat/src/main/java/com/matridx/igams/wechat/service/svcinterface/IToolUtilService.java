package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.BfglDto;
import com.matridx.igams.wechat.dao.entities.BfglModel;

public interface IToolUtilService extends BaseBasicService<BfglDto, BfglModel>{

	/**
	 * 根据患者姓名查询报告
	 * @param filePath
	 * @return
	 */
	boolean getPdfReport(String filePath);
	
}
