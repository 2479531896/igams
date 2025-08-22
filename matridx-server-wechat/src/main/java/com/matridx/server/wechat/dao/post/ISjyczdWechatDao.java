package com.matridx.server.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjyczdWechatDto;
import com.matridx.server.wechat.dao.entities.SjyczdWechatModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ISjyczdWechatDao extends BaseBasicDao<SjyczdWechatDto, SjyczdWechatModel> {

}
