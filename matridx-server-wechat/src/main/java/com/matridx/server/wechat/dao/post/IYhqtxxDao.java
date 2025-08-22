package com.matridx.server.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.YhqtxxDto;
import com.matridx.server.wechat.dao.entities.YhqtxxModel;


@Mapper
public interface IYhqtxxDao extends BaseBasicDao<YhqtxxDto, YhqtxxModel>{

}
