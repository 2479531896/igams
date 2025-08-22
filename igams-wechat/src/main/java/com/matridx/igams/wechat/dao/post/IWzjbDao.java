package com.matridx.igams.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.WzjbDto;
import com.matridx.igams.wechat.dao.entities.WzjbModel;

@Mapper
public interface IWzjbDao extends BaseBasicDao<WzjbDto, WzjbModel> {
}
