package com.matridx.igams.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.KdxxDto;
import com.matridx.igams.wechat.dao.entities.KdxxModel;

public interface IKdxxService extends BaseBasicService<KdxxDto, KdxxModel>{
	/**
	 * 在顺丰路由信息表中插入路由节点信息
	 * @param routeList
	 * @return
	 */
	boolean insertList(List<KdxxDto> routeList);

	/**
	 * 获取某个订单号下的路由节点个数
	 * @param kdxxDto
	 * @return
	 */
	int getCountByMailNo(KdxxDto kdxxDto);
}
