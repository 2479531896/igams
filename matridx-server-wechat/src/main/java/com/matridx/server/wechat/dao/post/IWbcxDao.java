package com.matridx.server.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WbcxModel;

@Mapper
public interface IWbcxDao extends BaseBasicDao<WbcxDto, WbcxModel>{

}
