package com.matridx.server.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjyzDto;
import com.matridx.server.wechat.dao.entities.SjyzModel;

@Mapper
public interface ISjyzDao extends BaseBasicDao<SjyzDto, SjyzModel>{

}
