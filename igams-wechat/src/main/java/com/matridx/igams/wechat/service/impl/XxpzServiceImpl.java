package com.matridx.igams.wechat.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.XxpzDto;
import com.matridx.igams.wechat.dao.entities.XxpzModel;
import com.matridx.igams.wechat.dao.post.IXxpzDao;
import com.matridx.igams.wechat.service.svcinterface.IXxpzService;

@Service
public class XxpzServiceImpl extends BaseBasicServiceImpl<XxpzDto, XxpzModel, IXxpzDao> implements IXxpzService{

	/**
	 * 获取信息配置
	 * @param xxpzDto
	 * @return
	 */
	public List<XxpzDto> getXxpzList(XxpzDto xxpzDto){
		return dao.getXxpzList(xxpzDto);
	}
}
