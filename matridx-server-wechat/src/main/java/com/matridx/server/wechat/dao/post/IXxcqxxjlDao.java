package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.XxcqxxjlDto;
import com.matridx.server.wechat.dao.entities.XxcqxxjlModel;

@Mapper
public interface IXxcqxxjlDao extends BaseBasicDao<XxcqxxjlDto, XxcqxxjlModel>{

	/**
	 * 获取最近3条记录正确数量
	 * @param xxcqxxjlDto
	 * @return
	 */
	List<XxcqxxjlDto> getRecentCorrect(XxcqxxjlDto xxcqxxjlDto);

}
