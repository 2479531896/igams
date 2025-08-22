package com.matridx.server.wechat.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.WbzxDto;
import com.matridx.server.wechat.dao.entities.WbzxModel;
import com.matridx.server.wechat.dao.post.IWbzxDao;
import com.matridx.server.wechat.service.svcinterface.IWbzxService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class WbzxServiceImpl extends BaseBasicServiceImpl<WbzxDto, WbzxModel, IWbzxDao> implements IWbzxService{

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertDto(WbzxDto wbzxDto)
	{
		if (StringUtil.isBlank(wbzxDto.getZxid()))
		{
			wbzxDto.setZxid(StringUtil.generateUUID());
		}
		int result = dao.insert(wbzxDto);
		if (result == 0)
			return false;

		return true;
	}
	
	/**
	 * 根据资讯类型和资讯子类型获取微信资讯列表
	 * @param wbzxDto
	 * @return
	 */
	public List<WbzxDto> getWsWechatNewsList(WbzxDto wbzxDto){
		return dao.getWsWechatNewsList(wbzxDto);
	}
}
