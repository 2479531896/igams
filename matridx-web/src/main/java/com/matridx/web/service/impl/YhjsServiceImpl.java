package com.matridx.web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.web.dao.entities.XtyhDto;
import com.matridx.web.dao.entities.YhjsDto;
import com.matridx.web.dao.entities.YhjsModel;
import com.matridx.web.dao.post.IYhjsDao;
import com.matridx.web.service.svcinterface.IYhjsService;

@Service
public class YhjsServiceImpl extends BaseBasicServiceImpl<YhjsDto, YhjsModel, IYhjsDao> implements IYhjsService{

	/**
	 * 获取可选用户
	 *@param xtyhDto
	 *@return
	 */
	public List<XtyhDto> getPagedOptionalList(XtyhDto xtyhDto){
		return dao.getPagedOptionalList(xtyhDto);
	}
	
	/**
	 * 获取已选用户
	 *@param xtyhDto
	 *@return
	 */
	public List<XtyhDto> getPagedSelectedList(XtyhDto xtyhDto){
		return dao.getPagedSelectedList(xtyhDto);
	}
	
	/**
	 * 添加用户
	 *@param yhjsDto
	 *@return
	 */
	public boolean toSelected(YhjsDto yhjsDto){
		return dao.toSelected(yhjsDto);
	}
	
	/**
	 * 去除用户
	 *@param xtjsDto
	 *@return
	 */
	public boolean toOptional(YhjsDto yhjsDto){
		return dao.toOptional(yhjsDto);
	}
}
