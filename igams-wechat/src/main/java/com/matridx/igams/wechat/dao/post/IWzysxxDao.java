package com.matridx.igams.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.WzysxxDto;
import com.matridx.igams.wechat.dao.entities.WzysxxModel;

import java.util.List;

@Mapper
public interface IWzysxxDao extends BaseBasicDao<WzysxxDto, WzysxxModel>{

    boolean insertDtoList(List<WzysxxDto> wzysxxDtos);

    List<WzysxxDto> getLlh(WzysxxDto wzysxxDto);

    List<WzysxxDto> getDtoList(WzysxxDto wzysxxDto);
}
