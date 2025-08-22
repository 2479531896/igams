package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.HzxxDto;
import com.matridx.server.wechat.dao.entities.HzxxModel;

public interface IHzxxService extends BaseBasicService<HzxxDto, HzxxModel>{

	/**
	 * 患者信息统计
	 * @param hzxxDto
	 * @return
	 */
	List<HzxxDto> getPatientStatis(HzxxDto hzxxDto);

	/**
	 * 修改患者信息
	 * @param hzxxDto
	 * @return
	 */
	boolean updatePatient(HzxxDto hzxxDto);

	/**
	 * 新增患者信息
	 * @param hzxxDto
	 * @return
	 */
	boolean insertPatient(HzxxDto hzxxDto);



}
