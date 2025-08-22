package com.matridx.server.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.XxpzDto;
import com.matridx.server.wechat.dao.entities.XxpzModel;

@Mapper
public interface IXxpzDao extends BaseBasicDao<XxpzDto, XxpzModel>{

}
