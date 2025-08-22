package com.matridx.igams.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.NyysxxDto;
import com.matridx.igams.wechat.dao.entities.NyysxxModel;

import java.util.List;

@Mapper
public interface INyysxxDao extends BaseBasicDao<NyysxxDto, NyysxxModel>{

    boolean insertDtoList(List<NyysxxDto> nyysxxDto);

    List<NyysxxDto> getNyysxxList(NyysxxDto nyysxxDto);
}
