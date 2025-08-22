package com.matridx.web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.web.dao.entities.XtjsDto;
import com.matridx.web.dao.entities.XtjsModel;
import com.matridx.web.dao.post.IXtjsDao;
import com.matridx.web.service.svcinterface.IXtjsService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class XtjsServiceImpl extends BaseBasicServiceImpl<XtjsDto, XtjsModel, IXtjsDao> implements IXtjsService{
	/**
	 * 获取系统角色列表同时根据用户ID标识用户已有的角色信息
	 * @param yhid
	 * @return
	 */
	public List<XtjsDto> getAllJsAndChecked(String yhid){
		XtjsDto xtjsDto = new XtjsDto();
		xtjsDto.setYhid(yhid);
		return dao.getAllJsAndChecked(xtjsDto);
	}
	
	/**
	 * 获取除自己以外的角色列表
	 * @param xtjsModel
	 * @return
	 */
	public List<XtjsModel> getModelListExceptSelf(XtjsModel xtjsModel){
		return dao.getModelListExceptSelf(xtjsModel);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insert(XtjsModel xtjsModel){
		xtjsModel.setJsid(StringUtil.generateUUID());
		int result = dao.insert(xtjsModel);

        return result > 0;
    }
}
