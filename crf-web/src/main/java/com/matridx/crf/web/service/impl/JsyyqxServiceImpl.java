package com.matridx.crf.web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.crf.web.dao.entities.JsyyqxDto;
import com.matridx.crf.web.dao.entities.JsyyqxModel;
import com.matridx.crf.web.dao.post.IJsyyqxDao;
import com.matridx.crf.web.service.svcinterface.IJsyyqxService;

@Service
public class JsyyqxServiceImpl extends BaseBasicServiceImpl<JsyyqxDto, JsyyqxModel, IJsyyqxDao> implements IJsyyqxService{
	
	/**
	 * 查询已选的医院
	 */
	@Override
	public List<JsyyqxDto> getPagedYxYyxxList(JsyyqxDto jsyyqxDto) {
		
		return dao.getPagedYxYyxxList(jsyyqxDto);
	}
	/**
	 * 查询医院列表(除本身)
	 * @param jgxxDto
	 * @return
	 */
	public List<JsyyqxDto> getPagedOtherYyxxList(JsyyqxDto jsyyqxDto){
		return dao.getPagedOtherYyxxList(jsyyqxDto);
	}
	
	/**
	 * 添加角色医院
	 * @param jsyyqxDto
	 * @return
	 */
	@Override
	public boolean toSelectedYy(JsyyqxDto jsyyqxDto) {
		boolean result = dao.toSelectedYy(jsyyqxDto);
		return result;
	}
	
	/**
	 * 去除角色医院
	 * @param jsyyqxDto
	 * @return
	 */
	@Override
	public boolean toOptionalYy(JsyyqxDto jsyyqxDto) {
		boolean result = dao.toOptionalYy(jsyyqxDto);
		return result;
	}
	
}
