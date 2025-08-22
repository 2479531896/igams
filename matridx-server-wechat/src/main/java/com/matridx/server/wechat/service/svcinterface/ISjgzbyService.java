package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjgzbyDto;
import com.matridx.server.wechat.dao.entities.SjgzbyModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;

public interface ISjgzbyService extends BaseBasicService<SjgzbyDto, SjgzbyModel>{
	/**
	 * 根据送检信息新增关注病原
	 * @param sjxxDto
	 * @return
	 */
    int insertBySjxx(SjxxDto sjxxDto);
	
	/**
	 * 根据页面的输入列表更新关注病原
	 * @param sjgzbyDtos
	 * @return
	 */
	//public int updateBySjxx(SjxxDto sjxxDto);
	
	/**
	 * 获取关注病原的String清单
	 * @param sjgzbyDto
	 * @return
	 */
    List<String> getStringList(SjgzbyDto sjgzbyDto);

	/**
	 * 根据送检信息新增关注病原
	 * @param sjxxDto
	 * @return
	 */
    int insertBySjxxDto(SjxxDto sjxxDto);
}
