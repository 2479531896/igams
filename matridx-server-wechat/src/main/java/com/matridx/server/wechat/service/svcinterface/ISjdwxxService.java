package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjdwxxDto;
import com.matridx.server.wechat.dao.entities.SjdwxxModel;

public interface ISjdwxxService extends BaseBasicService<SjdwxxDto, SjdwxxModel>{
	
	/**
	* 查询送检单位信息
	* @return
	*/
    List<SjdwxxDto> getSjdwxx();
	/**
	 * 获取所有科室
	 * @return
	 */
    List<SjdwxxDto> getAllSjdwxx();
		
	/**
	* 获取本地推送医院信息
	* @param request
	* @return
	*/
    boolean createHis(HttpServletRequest request);
	
	/**
	* 查询送检单位列表
	* @return
	*/
	List<SjdwxxDto> selectSjdwList();
	
}

