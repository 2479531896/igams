package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjlczzDto;
import com.matridx.server.wechat.dao.entities.SjlczzModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;

public interface ISjlczzService extends BaseBasicService<SjlczzDto, SjlczzModel>{

	/**
	 * 根据送检信息新增临床症状
	 * @param sjxxDto
	 * @return
	 */
	boolean insertBySjxx(SjxxDto sjxxDto);

	/**
	 * 根据送检信息修改临床症状
	 * @param sjxxDto
	 * @return
	 */
	//boolean updateBySjxx(SjxxDto sjxxDto);
	
	/**
	 * 获取检测项目的String清单
	 * @param sjlczzDto
	 * @return
	 */
    List<String> getStringList(SjlczzDto sjlczzDto);

	/**
	 * 根据送检ID查询送检临床症状
	 * @param sjxxDto
	 * @return
	 */
    List<SjlczzDto> selectLczzBySjid(SjxxDto sjxxDto);

	/**
	 * 根据送检信息新增临床症状
	 * @param sjxxDto
	 * @return
	 */
	boolean insertBySjxxDto(SjxxDto sjxxDto);

}
