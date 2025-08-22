package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.MrzkDto;
import com.matridx.igams.wechat.dao.entities.MrzkModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMrzkDao extends BaseBasicDao<MrzkDto, MrzkModel> {
}
