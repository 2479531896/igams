package com.matridx.igams.experiment.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmscxxDto;
import com.matridx.igams.experiment.dao.entities.XmscxxModel;
import com.matridx.igams.experiment.dao.post.IXmscxxDao;
import com.matridx.igams.experiment.service.svcinterface.IXmscxxService;

@Service
public class XmscxxServiceImpl extends BaseBasicServiceImpl<XmscxxDto, XmscxxModel, IXmscxxDao> implements IXmscxxService{
	
	/**
	 * 将项目删除信息加入到删除信息表
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertByXmglDto(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		XmscxxDto xmscxxDto = new XmscxxDto();
		xmscxxDto.setXmid(xmglDto.getXmid());
		xmscxxDto.setFxmid(xmglDto.getFxmid());
		xmscxxDto.setScry(xmglDto.getScry());
		return insert(xmscxxDto);
	}
	
	/**
	 * 获取删除项目列表
	 */
	@Override
	public List<XmglDto> getRecoverDtoList(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		return dao.getRecoverDtoList(xmglDto);
	}

	/**
	 * 根据项目ID删除数据
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteByXmglDto(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		int result = dao.deleteByXmglDto(xmglDto);
		return result != 0;
	}
}
