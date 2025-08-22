package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.WbzxDto;
import com.matridx.server.wechat.dao.entities.WbzxModel;

@Mapper
public interface IWbzxDao extends BaseBasicDao<WbzxDto, WbzxModel>{

	/**
	 * 根据资讯类型和资讯子类型获取微信资讯列表
	 * @param wbzxDto
	 * @return
	 */
	List<WbzxDto> getWsWechatNewsList(WbzxDto wbzxDto);
}
