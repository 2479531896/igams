package com.matridx.igams.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.NyypxxDto;
import com.matridx.igams.wechat.dao.entities.NyypxxModel;

@Mapper
public interface INyypxxDao extends BaseBasicDao<NyypxxDto, NyypxxModel>{

}
