package com.matridx.igams.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.LsbDto;
import com.matridx.igams.wechat.dao.entities.LsbModel;

@Mapper
public interface ILsbDao extends BaseBasicDao<LsbDto, LsbModel>{


}
