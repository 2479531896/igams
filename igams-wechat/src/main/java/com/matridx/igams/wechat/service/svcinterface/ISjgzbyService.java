package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjgzbyDto;
import com.matridx.igams.wechat.dao.entities.SjgzbyModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;

import java.util.List;

public interface ISjgzbyService extends BaseBasicService<SjgzbyDto, SjgzbyModel>{

	/**
	 * 根据送检信息新增关注病原
	 * @param sjxxDto
	 * @return
	 */
    int insertBySjxx(SjxxDto sjxxDto);
	/**
	 * 根据页面的输入列表登录数据库
	 * @param sjxxDto
	 * @return
	 */
    int insertSjgzby(SjxxDto sjxxDto);
	
	/**
	 * 根据送检id查询关注病原
	 * @param sjid
	 * @return
	 */
    List<String> getGzby(String sjid);


}
