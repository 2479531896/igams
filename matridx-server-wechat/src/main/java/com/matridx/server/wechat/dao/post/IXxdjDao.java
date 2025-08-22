package com.matridx.server.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.XxdjDto;
import com.matridx.server.wechat.dao.entities.XxdjModel;

@Mapper
public interface IXxdjDao extends BaseBasicDao<XxdjDto, XxdjModel>{
	
	/**
	 * 通过微信id删除
	 * @param wxid
	 * @return
	 */
    boolean deleteByWxid(String wxid);
}
