package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjlcznDto;
import com.matridx.igams.wechat.dao.entities.SjlcznModel;

public interface ISjlcznService extends BaseBasicService<SjlcznDto, SjlcznModel>{

	/**
	 * 将临床诊疗指南转为String类型
	 * @param sjid
	 * @return
	 */
    String getGuideToString(String sjid);
}
