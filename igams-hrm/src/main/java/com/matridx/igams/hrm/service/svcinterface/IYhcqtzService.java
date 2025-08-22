package com.matridx.igams.hrm.service.svcinterface;

import java.util.Map;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.YhcqtzDto;
import com.matridx.igams.hrm.dao.entities.YhcqtzModel;

public interface IYhcqtzService extends BaseBasicService<YhcqtzDto, YhcqtzModel>{

	/**
	 * 根据yhid获取用户出勤信息
	 */
	YhcqtzDto getCqxxByYhid(YhcqtzDto yhcqtzDto);
	
	/**
	 * 根据ids删除用户出勤信息
	 */
	boolean delYhcqtzxx(YhcqtzDto yhcqtzDto);

	/**
	 * 更新用户出勤通知信息
	 */
	Map<String,Object> updateYhcqtz(YhcqtzDto yhcqtzDto);

}
