package com.matridx.igams.wechat.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.KdxxDto;
import com.matridx.igams.wechat.dao.entities.KdxxModel;
import com.matridx.igams.wechat.dao.post.IKdxxDao;
import com.matridx.igams.wechat.service.svcinterface.IKdxxService;

@Service
public class KdxxServiceImpl extends BaseBasicServiceImpl<KdxxDto, KdxxModel, IKdxxDao> implements IKdxxService{
	/**
	 * 在顺丰路由信息表中插入路由节点信息
	 */
	@Override
	public boolean insertList(List<KdxxDto> routeList) {
		// TODO Auto-generated method stub
		return dao.insertList(routeList);
	}

	/**
	 * 获取订单号下的路由节点个数
	 */
	@Override
	public int getCountByMailNo(KdxxDto kdxxDto) {
		// TODO Auto-generated method stub
		return dao.getCountByMailNo(kdxxDto);
	}
}
