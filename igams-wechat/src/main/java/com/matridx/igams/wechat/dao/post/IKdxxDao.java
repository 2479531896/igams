package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.KdxxDto;
import com.matridx.igams.wechat.dao.entities.KdxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IKdxxDao extends BaseBasicDao<KdxxDto, KdxxModel>{
	/**
	 * 在顺丰路由信息表中插入路由节点信息
	 * @param routeList
	 * @return
	 */
	boolean insertList(List<KdxxDto> routeList);

	/**
	 * 获取订单号下路由节点个数
	 * @param kdxxDto
	 * @return
	 */
	int getCountByMailNo(KdxxDto kdxxDto);
}
